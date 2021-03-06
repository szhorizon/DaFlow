ARG HADOOP_VERSION=2.8.4 
FROM abhioncbr/daflow-hadoop-base:${HADOOP_VERSION}

ENV HIVE_HOME /opt/hive
ENV PATH $HIVE_HOME/bin:$PATH
ENV HADOOP_HOME /opt/hadoop-$HADOOP_VERSION

WORKDIR /opt

ARG HIVE_VERSION=2.3.3
ARG HIVE_URL=https://archive.apache.org/dist/hive/hive-$HIVE_VERSION/apache-hive-$HIVE_VERSION-bin.tar.gz
ENV HIVE_VERSION ${HIVE_VERSION}
ENV HIVE_URL ${HIVE_URL}

#Install Hive MySQL, PostgreSQL JDBC
RUN echo "Hive URL is :${HIVE_URL}" && wget ${HIVE_URL} -O hive.tar.gz && \
	tar -xzvf hive.tar.gz && mv *hive*-bin hive && \
        ln -s /usr/share/java/mysql-connector-java.jar $HIVE_HOME/lib/mysql-connector-java.jar && \
	wget https://jdbc.postgresql.org/download/postgresql-9.4.1212.jar -O $HIVE_HOME/lib/postgresql-jdbc.jar && \
	rm hive.tar.gz && mkdir -p /var/daflow/ws/docker/daflow/hadoop/hive_base/target/

#Spark should be compiled with Hive to be able to use it
#hive-site.xml should be copied to $SPARK_HOME/conf folder

#Custom configuration goes here
ADD conf/hive-site.xml $HADOOP_CONF_DIR
ADD conf/beeline-log4j2.properties $HIVE_HOME/conf
ADD conf/hive-env.sh $HIVE_HOME/conf
ADD conf/hive-exec-log4j2.properties $HIVE_HOME/conf
ADD conf/hive-log4j2.properties $HIVE_HOME/conf
ADD conf/ivysettings.xml $HIVE_HOME/conf
ADD conf/llap-daemon-log4j2.properties $HIVE_HOME/conf

# Setup DaFlow Library jars
ADD target/demo /var/daflow/ws/daflow-examples/demo

ENV DAFLOW_BUNDLE=/var/daflow/demo/artifacts/daflow-*.jar

COPY startup.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/startup.sh

COPY entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/entrypoint.sh

ENV PATH $HIVE_HOME/bin/:$PATH

ENTRYPOINT ["entrypoint.sh"]
CMD startup.sh
