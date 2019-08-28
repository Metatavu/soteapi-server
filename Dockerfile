FROM jboss/wildfly:17.0.1.Final

ARG WILDFLY_VERSION=17.0.1.Final
ARG MYSQL_MODULE_VERSION=8.0.17

ENV timezone=Europe/Helsinki

ADD --chown=jboss target/*.war /opt/jboss/wildfly/standalone/deployments/app.war
ADD --chown=jboss ./docker/entrypoint.sh /opt/docker/entrypoint.sh 
ADD --chown=jboss ./docker/jboss-cli.properties /opt/docker/jboss-cli.properties
ADD --chown=jboss ./docker/host.cli /opt/docker/host.cli
ADD --chown=jboss ./docker/kubernets-jgroups.cli /opt/docker/kubernets-jgroups.cli
ADD --chown=jboss ./docker/jdbc.cli /opt/docker/jdbc.cli
ADD --chown=jboss ./docker/interfaces.cli /opt/docker/interfaces.cli
ADD --chown=jboss ./docker/env.cli /opt/docker/env.cli
RUN chmod a+x /opt/docker/entrypoint.sh

RUN curl -o /tmp/mysql-module.zip -L https://static.metatavu.io/wildfly/wildfly-${WILDFLY_VERSION}-mysql-module-${MYSQL_MODULE_VERSION}.zip
RUN unzip -o /tmp/mysql-module.zip -d /opt/jboss/wildfly/

RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/docker/host.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/docker/jdbc.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/docker/kubernets-jgroups.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/docker/interfaces.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/opt/docker/env.cli

USER root
RUN ln -snf /usr/share/zoneinfo/$timezone /etc/localtime && echo $timezone > /etc/timezone
USER jboss

RUN rm /tmp/*.zip

EXPOSE 8080
EXPOSE 7600
EXPOSE 7800

CMD "/opt/docker/entrypoint.sh"