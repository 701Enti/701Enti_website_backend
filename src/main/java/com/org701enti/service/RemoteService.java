//        701Enti MIT License
//
//        Copyright (c) 2025 701Enti
//
//        Permission is hereby granted, free of charge, to any person obtaining a copy
//        of this software and associated documentation files (the "Software"), to deal
//        in the Software without restriction, including without limitation the rights
//        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//        copies of the Software, and to permit persons to whom the Software is
//        furnished to do so, subject to the following conditions:
//
//        The above copyright notice and this permission notice shall be included in all
//        copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//        SOFTWARE.
package com.org701enti.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class RemoteService {
    private static final String GITHUB_GIT_CLONE_BASE_URL = "https://github.com";//GitHub git clone请求头部
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";//GitHub API头部
    private static final String ORG_NAME = "701Enti";//在Github的组织名

    /**
     * 远程信息的常规定时更新
     */
    @Scheduled(fixedRate = 20 * 1000)
    public void remoteInformationUpdateRoutine() {
        Flux.just(
                        Mono.zip(
                                Mono.just(TaskData.TaskLabel.GET_REMOTE_REPOSITORY_INFORMATION),
                                Mono.fromCallable(RemoteService::getRemoteRepositoryInformation).subscribeOn(Schedulers.boundedElastic())
                        )
                )
                .flatMap(mono -> mono)
                .map(tuple -> new TaskData(tuple.getT1(), tuple.getT2()))//从元组提取标签和结果缓存到TaskData实例
                .subscribe(
                        taskData -> {

                        },
                        error -> log.error("remoteInformationUpdateRoutine无法完成,因为:", error)
                );
    }

    public class TaskData {
        @Getter
        private TaskLabel label;//任务标签,TaskLabel是一个枚举类型,用于识别任务以分类操作
        @Getter
        private Object result;//任务结果,为生产者提供的唯一对应结果

        public void setLabel(TaskLabel label) {
            this.label = label;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        public TaskData(TaskLabel label, Object result) {
            this.label = label;
            this.result = result;
        }

        /**
         * 任务标签,用于识别任务以分类操作
         */
        public enum TaskLabel {
            GET_REMOTE_REPOSITORY_INFORMATION,
        }

    }


    /**
     * 获取组织的所有存储库信息
     *
     * @return 请求Github API获得的响应实体数据
     */
    private static ResponseEntity<String> getRemoteRepositoryInformation() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("User-Agent", "701Enti-website-backend-" + "RemoteService");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                GITHUB_API_BASE_URL + "/orgs" + "/" + ORG_NAME + "/repos",
                HttpMethod.GET,
                requestEntity,
                String.class);
    }

}
