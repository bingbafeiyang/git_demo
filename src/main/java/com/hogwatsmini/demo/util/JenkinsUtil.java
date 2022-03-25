package com.hogwatsmini.demo.util;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class JenkinsUtil {
    public static void buildJob(String jobName,String userId,String remark,String testCommand) throws IOException, URISyntaxException {

        ClassPathResource classPathResource=new ClassPathResource("JenkinsConfigDir/hogwarts_test_jenkins_show.xml");
        InputStream inputStream=classPathResource.getInputStream();
        String jobConfigXml = FileUtil.getText(inputStream);
//        System.out.println(jobConfigXml);

        String baseUrl="http://stuq.ceshiren.com:8080/";
        String userName="hogwarts";
        String password="hogwarts123";
//        String jobName="test102";


        JenkinsHttpClient jenkinsHttpClient= new JenkinsHttpClient(new URI(baseUrl),userName,password);
        String jenkinsVersion=jenkinsHttpClient.getJenkinsVersion();
        System.out.println(jenkinsVersion);
        JenkinsServer jenkinsServer=new JenkinsServer(jenkinsHttpClient);
        jenkinsServer.updateJob(jobName,jobConfigXml,true);
        Map<String, Job> jobMap=jenkinsServer.getJobs();
        Job job=jobMap.get(jobName);
        Map<String,String> map =new HashMap<>();
        map.put("userId",userId);
        map.put("remark",remark);
        map.put("testCommand",testCommand);
        job.build(map,true);
        System.out.println("");
//        job.details();

    }
}
