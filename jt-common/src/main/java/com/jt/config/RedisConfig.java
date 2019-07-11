package com.jt.config;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

	@Value("${redis.nodes}")
	private String nodes;
	
	@Bean
	public JedisCluster jedisCluster() {
		HashSet<HostAndPort> nodesSet = new HashSet<>();
		String[] node = nodes.split(",");
		for (String h_pNode : node) {
			//ip:port
			String[] args = h_pNode.split(":");
			int port = Integer.parseInt(args[1]);
			HostAndPort hostAndPort = new HostAndPort(args[0], port);
			nodesSet.add(hostAndPort);
		}
		return new JedisCluster(nodesSet);
	}
	
}
