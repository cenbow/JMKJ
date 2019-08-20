<%@ page language='java' contentType='text/html; charset=utf-8'
	pageEncoding='utf-8'%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<c:if test='${urlParas == null}'>
	<c:set var='urlParas' value='' />
</c:if>
<c:if test='${(totalPage > 0) && (currentPage <= totalPage)}'>
	<c:set var='startPage' value='${currentPage - 4}' />
	<c:if test='${startPage < 1}'>
		<c:set var='startPage' value='1' />
	</c:if>
	<c:set var='endPage' value='${currentPage + 4}' />
	<c:if test='${endPage > totalPage}'>
		<c:set var='endPage' value='totalPage' />
	</c:if>
	<div style="margin-top: -10px;"
		class="layui-box layui-laypage layui-laypage-default"
		id="layui-laypage-6">

		<c:if test='${currentPage <= 8}'>
			<c:set var='startPage' value='1' />
		</c:if>

		<c:if test='${(totalPage - currentPage) < 8}'>
			<c:set var='endPage' value='${totalPage}' />
		</c:if>

		<c:choose>
			<c:when test='${currentPage == 1}'>
			</c:when>
			<c:otherwise>
				<a href="${actionUrl}${currentPage - 1}${urlParas}">上一页</a>
			</c:otherwise>
		</c:choose>

		<c:if test='${currentPage > 8}'>
			<a href="${actionUrl}${1}${urlParas}"> ${1}</a>
			<a href="${actionUrl}${2}${urlParas}"> ${2}</a>
			<span>…</span>
		</c:if>

		<c:forEach begin='${startPage}' end='${endPage}' var='i'>
			<c:choose>
				<c:when test='${currentPage == i}'>
					<span class="layui-laypage-curr"><em
						class="layui-laypage-em"></em><em>${i}</em></span>
				</c:when>
				<c:otherwise>
					<a href="${actionUrl}${i}${urlParas}">${i}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:if test='${(totalPage - currentPage) >= 8}'>
			<span>…</span>
			<a href="${actionUrl}${totalPage - 1}${urlParas}">${totalPage - 1}</a>
			<a href="${actionUrl}${totalPage}${urlParas}">${totalPage}</a>
		</c:if>

		<c:choose>
			<c:when test='${currentPage == totalPage}'>
			</c:when>
			<c:otherwise>
				<a href="${actionUrl}${currentPage + 1}${urlParas}">下一页</a>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>