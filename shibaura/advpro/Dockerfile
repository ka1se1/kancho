FROM ubuntu:22.04
LABEL maintainer="Tsuyoshi Nakajima <tsnaka@shibaura-it.ac.jp>"

COPY startup.sh /startup.sh
RUN chmod 744 /startup.sh

RUN apt-get update && apt-get upgrade -y
RUN apt-get install openjdk-17-jdk curl openssh-server -y --no-install-recommends
RUN useradd -M tomcat
RUN curl -O https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.73/bin/apache-tomcat-9.0.73.tar.gz
RUN tar -xzvf apache-tomcat-9.0.73.tar.gz
RUN mkdir /usr/local/java/
RUN mv apache-tomcat-9.0.73 /usr/local/java/apache-tomcat-9.0.73
RUN chown -R tomcat:tomcat /usr/local/java/apache-tomcat-9.0.73/

RUN mkdir /var/run/sshd
RUN echo 'root:advpro2' | chpasswd
RUN sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -i 's/#PasswordAuthentication/PasswordAuthentication/' /etc/ssh/sshd_config
RUN echo "Completed!"

CMD ["/startup.sh"]