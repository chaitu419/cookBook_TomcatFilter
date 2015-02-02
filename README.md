To activate filter need add

	<filter>
        <filter-name>RequestCatcher</filter-name>
        <filter-class>com.atcsibir.filter.RequestCatcher</filter-class>
    </filter>

	<filter-mapping>
        <filter-name>RequestCatcher</filter-name>
        <url-pattern>/ws/*</url-pattern>
    </filter-mapping>

to %CATALINA_HOME%/conf/web.xml