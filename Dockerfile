FROM tomcat:8.5.30

ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone



COPY ./rongke-web/target/rongke-web-1.0-SNAPSHOT.war /app/
COPY ./tmp/ext/* /app/tmp/ext/
COPY ./tmp/security/* /app/tmp/security/
COPY ./catalina.properties  /usr/local/tomcat/conf/catalina.properties 

WORKDIR /app


RUN rm -fr /usr/local/tomcat/webapps/ROOT


RUN unzip -oq  /app/rongke-web-1.0-SNAPSHOT.war -d /usr/local/tomcat/webapps/ROOT

RUN cp /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/com/rongke/web/yibaolib/* /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/ -f

RUN cp /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/com/rongke/web/dhlib/* /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/ -f



EXPOSE 8080

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]