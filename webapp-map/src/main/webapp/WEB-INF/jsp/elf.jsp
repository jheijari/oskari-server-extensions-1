<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>ELF Showcase Application</title>
    <meta charset="utf-8" />
    <link rel="shortcut icon" href="http://locationframework.eu/sites/default/files/elffavicon_0.jpg" type="image/jpeg" />
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>

    <!-- ############# css ################# -->
    <link type="text/css" rel="stylesheet" href="//fonts.googleapis.com/css?family=Open+Sans:400,400italic,700,700italic,800,800italic,600italic,600" />
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari/resources/css/forms.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari/resources/css/portal.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/icons.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/css/overwritten.css"/>
    <style type="text/css">
        @media screen {

            #login {
                margin-left: 5px;
            }

            #login input[type="text"], #login input[type="password"] {
                width: 90%;
                margin-bottom: 5px;
                background-image: url("/Oskari${path}/images/forms/input_shadow.png");
                background-repeat: no-repeat;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: #878787;
                font: 13px/100% Arial,sans-serif;
            }
            #login input[type="submit"] {
                width: 90%;
                margin-bottom: 5px;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: #878787;
                font: 13px/100% Arial,sans-serif;
            }
            #login p.error {
                font-weight: bold;
                color : red;
                margin-bottom: 10px;
            }

            #login a {
                color: #FFF;
                padding: 5px;
            }
        }
    </style>
    <!-- ############# /css ################# -->
</head>
<body>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="locale"/>
<nav id="maptools">
    <a href="http://www.locationframework.eu" target="_blank">
        <div id="logobar">
        </div>
    </a>
    <div id="menubar">
    </div>
    <div id="divider">
    </div>
    <div id="toolbar">
    </div>
     <div id="login">
        <c:set var="user" value="fi.nls.oskari.domain.User" />
        <c:choose>
            <c:when test="${!empty _logout_uri}">
                <a href="${_logout_uri}"><spring:message code="logout" text="Logout" /></a>
                <br><br>
                <a href="https://security.locationframework.eu/administration" target="_blank">administration</a>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${!empty loginState}">
                        <p class="error"><spring:message code="invalid_password_or_username" text="Invalid password or username!" /></p>
                    </c:when>
                </c:choose>
                <form action='j_security_check' method="post" accept-charset="UTF-8">
                    <input size="16" id="username" name="j_username" type="text" placeholder="<spring:message code="username" text="Username" />" autofocus
                           required>
                    <input size="16" id="password" name="j_password" type="password" placeholder="<spring:message code="password" text="Password" />" required>
                    <input type="submit" id="submit" value="<spring:message code="login" text="Log in" />">
                </form>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="langSelector">
    </div>
</nav>
<div id="contentMap">
    <div id="mapdiv"></div>
</div>


<!-- ############# Javascript ################# -->

<!--  OSKARI -->

<script type="text/javascript">
    var ajaxUrl = '${ajaxUrl}';
    var language = '${language}';
    var preloaded = ${preloaded};
    var controlParams = ${controlParams};
</script>

<script type="text/javascript"
        src="/Oskari/bundles/bundle.js">
</script>

<c:if test="${preloaded}">
    <!-- Pre-compiled application JS, empty unless created by build job -->
    <script type="text/javascript"
            src="/Oskari${path}/oskari.min.js">
    </script>
    <!-- Minified CSS for preload -->
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/oskari.min.css"
            />
    <%--language files --%>
    <script type="text/javascript"
            src="/Oskari${path}/oskari_lang_${language}.js">
    </script>
</c:if>

<script type="text/javascript"
        src="/Oskari${path}/index.js">
</script>


<!-- ############# /Javascript ################# -->
</body>
</html>
