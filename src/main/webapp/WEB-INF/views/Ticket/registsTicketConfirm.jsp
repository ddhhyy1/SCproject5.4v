<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/title.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/content.css">
<meta charset="UTF-8">
<title>사용&예약 확인 후 결제</title>
</head>
<body>
<%@ include file="../include/header.jsp" %>
<center>
	<table width="75%" border="0" cellspacing="0" cellpadding="20">
		<tr>
			<td class="titlebox1">
				<span class="title01"><img src="${pageContext.request.contextPath} /resources/img/book5.png" >STUDY CAFE</span>
			</td>
		</tr>
		<tr>
			<td class="titlebox1">
				<span class="title02">좌석등록 및 예약 확인</span>
			</td>
		</tr>
		<tr>
			<td>
				<center>
				<form action="sTicketReservComplete">
				<table width="80%" border="0" cellspacing="0" cellpadding="10">
					<tr class="contentbox">
						<td class="content">
							<center>						
							<table border="0" cellspacing="0" cellpadding="10">
									<tr>
										<td colspan="2" align="center" class="content_text01">
										주문 상품 정보
										</td>
									</tr>
									<tr>
										<td><span class="content_text01">아 이 디 : </span>
										<span class="title02">&nbsp;&nbsp;&nbsp;&nbsp;<%= sessionId %></span>
										
										</td>
									</tr>
									<tr>
										<td><span class="content_text01">이용날짜 : </span>
										&nbsp;&nbsp;${selectedDate }
										
										</td>
									</tr>
									
									<tr>
										<td><span class="content_text01">지정좌석 : </span>
										&nbsp;&nbsp;${seatNo}번좌석
										
										</td>
									</tr>
									<tr>
										<td><span class="content_text01">이용시간 : </span>
										&nbsp;&nbsp;${totalHour}시간
										
										</td>
									</tr>
										<tr>
										<td><span class="content_text01">지정한 시간대 : </span>
										&nbsp;&nbsp;${startTime}:00 ~ ${endTime}:00
										
										</td>
									</tr>
										</tr>
										<tr>
										<td><span class="content_text01">예약 후 보유시간:</span>
										&nbsp;&nbsp;${remainTime}시간
										</td>
									</tr>
										<input type="hidden" name = "remainTime" value="${remainTime}">
										<input type="hidden" name = "seatNo" value="${seatNo}">
										<input type="hidden" name = "selectedDate" value="${selectedDate}">
										<input type="hidden" name = "selectedTime" value="${selectedTime}">
										<input type="hidden" name = "startTime" value="${startTime}">
										<input type="hidden" name = "endTime" value="${endTime}">
									<tr>		
										<td colspan="2">
											<input class="button_type01" type="submit" value="결제하기" >&nbsp;&nbsp;
											<input class="button_type01" type="button" value="뒤로" onclick="window.history.back() ">
										</td>
									</tr>
									</form>			
							</table>
							</center>							
						</td>						
					</tr>
					
				</table>
				</center>			
			</td>
		</tr>
	</table>
	</center>
<%@ include file="../include/footer.jsp" %>
</body>
</html>