package com.studycafe.prac.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.studycafe.prac.dao.MemberDao;
import com.studycafe.prac.dao.TodayTicketDao;
import com.studycafe.prac.dto.SubscriptionTicketDto;
import com.studycafe.prac.dto.memberDto;
import com.studycafe.prac.dto.seatDto;

@Controller
public class ReservInfoController {

	
	@Autowired
	private SqlSession sqlSession;
	
	
	@RequestMapping(value="/ReservInfoList")
	public String ReservInfoList(Model model,HttpSession session) {
	
		
		String sessionId = (String) session.getAttribute("userId");
		TodayTicketDao tdao = sqlSession.getMapper(TodayTicketDao.class);
		
		MemberDao mdao = sqlSession.getMapper(MemberDao.class);
		SubscriptionTicketDto stDto = tdao.getSTicketInfo(sessionId);
		List<seatDto> AllReservInfo= tdao.getAllReservInfo(sessionId);
		
		
		memberDto memberDto = mdao.getMemberInfo(sessionId);
		model.addAttribute("memberDto", memberDto);
		model.addAttribute("stDto", stDto);
		model.addAttribute("AllReservInfo", AllReservInfo);
		
		
		return "Ticket/ReservInfoList";
		
	}
	@RequestMapping(value="/ReservInfoView")
	public String ReservInfoView(Model model,HttpSession session,HttpServletRequest request) {

		
		
		String sessionId = (String) session.getAttribute("userId");
		String tempNo = request.getParameter("tempUsingNo");
		
		MemberDao mdao = sqlSession.getMapper(MemberDao.class);
		TodayTicketDao tdao = sqlSession.getMapper(TodayTicketDao.class);
		
		seatDto sDto = tdao.getReservInfo(tempNo);

		String selectedDate = sDto.getSelectedDate();
		
		String year= selectedDate.substring(0, 4);
		String month= selectedDate.substring(4, 6);
		String day= selectedDate.substring(6, 8);
		
		model.addAttribute("selectedDate",selectedDate);
		model.addAttribute("day",day);
		model.addAttribute("month",month);
		model.addAttribute("year",year);
		model.addAttribute("sDto",sDto);
		
			return "Ticket/ReservInfoView";
		
}	
	@RequestMapping(value="/cancelReserv")
	public String cancelReserv2(HttpSession session,HttpServletRequest request,HttpServletResponse response) {
		
		String sessionId = (String) session.getAttribute("userId");
		String tempNo = request.getParameter("tempUsingNo");
		String seatNo = request.getParameter("seatNo");
		String selectedDate = request.getParameter("selectedDate");
		
		TodayTicketDao tdao = sqlSession.getMapper(TodayTicketDao.class);
		MemberDao mdao = sqlSession.getMapper(MemberDao.class);
		seatDto sDto = tdao.getReservInfo(tempNo);
		SubscriptionTicketDto subscrDto= tdao.getSTicketInfo(sessionId);
		
		memberDto memberDto = mdao.getMemberInfo(sessionId);
		int userPoint = Integer.parseInt(memberDto.getUserPoint().toString());//기존 유저 포인트를 계산해주기 위해 int값변환
		
		int usingTicket = Integer.parseInt(memberDto.getUsingTicket().toString());//사용하고 있던 당일권 숫자가져옴.
		int intUsingTicket = usingTicket;//불러온 당일권 값을 0으로 바꾸기전에 사용하기 위해 다른 이름으로 저장
		String strUsingTicket = Integer.toString(usingTicket);// 당일권값을 비교하기 위해 str로 변환
		usingTicket = 0; //예약이 취소되면 이용권은 0으로 변환
		String zeroUsingTicket = Integer.toString(usingTicket); //0으로 변환된 int를 다시 str로 변환하여 저장
		String salesNo = sDto.getSalesNo();//매출테이블 번호 뽑아오기
		
		int ticketName = Integer.parseInt(sDto.getTicketName().toString());
		
		
		
			if(intUsingTicket>0 && intUsingTicket<50) { //당일권 유저
				
		
			//당일권의 환불할 포인트 계산하기		
				int t;
				String[] TodayPrice = {"2,000","3,000","5,000","6,000","7,000"};
				String[] TodayTime = {"1","2","4","6","8"};
				for(t=0;t<5;t++) {
					if(TodayTime[t].equals(strUsingTicket)) {//str로 변환한 당일권값이 TodayTime의 인덱스 값과 일치할때만 포인트 저장
						String returnPoint = TodayPrice[t];
						String rPoint = returnPoint.replaceAll(",", "");//저장된 포인트의 , 쉼표 다 제거
						int intRPoint = Integer.parseInt(rPoint); //계산을 위해 int값으로 변환
						String finalUserRPoint = Integer.toString(userPoint+intRPoint);//환불을 위해 기존 포인트에 환불될 포인트 더 한후 str값으로변환
						tdao.returnUserPointTicket(sessionId, finalUserRPoint ,zeroUsingTicket);//포인트와 티켓값 업데이트 해줌
					}
				}
				tdao.cancelReservSaleTbl(salesNo); //매출 테이블 매출 제거
		
				tdao.cancelReservSeatTbl(tempNo); //전체적좌석 테이블 정보 제거
				tdao.cancelReservResvTbl(sessionId, seatNo, selectedDate); //예약된 시간대 및 좌석 제거
				
				try {
           			response.setContentType("text/html; charset=UTF-8");      
           	        PrintWriter out;
           			out = response.getWriter();
           			out.println("<script>alert('취소 완료!');window.location.href = 'ReservInfoList'</script>");
           		    out.flush();
           		} catch (IOException e) {
           			// TODO Auto-generated catch block
           			e.printStackTrace();
           		}
				
			}else {		
				
				int remainTime = Integer.parseInt(subscrDto.getSremainTime().toString());
				int returnedRemainTime= remainTime + ticketName;
				String rrTime = Integer.toString(returnedRemainTime);
				
				tdao.cancelReservSeatTbl(tempNo); //전체적좌석 테이블 정보 제거
				tdao.cancelReservResvTbl(sessionId, seatNo, selectedDate); //예약된 시간대 및 좌석 제거
				tdao.returnRemainTime(sessionId, rrTime);
			
				try {
           			response.setContentType("text/html; charset=UTF-8");      
           	        PrintWriter out;
           			out = response.getWriter();
           			out.println("<script>alert('취소 완료!');window.location.href = 'ReservInfoList'</script>");
           		    out.flush();
           		} catch (IOException e) {
           			// TODO Auto-generated catch block
           			e.printStackTrace();
           		}
			}
		
		return "redirect:ReservInfoList";
	}
	

	@RequestMapping(value = "changeDateSeat")
	public String changeDateSeat() {
		
		return "Ticket/changeDateSeat";
	}
}
