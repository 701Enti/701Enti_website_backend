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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RemoteService {
    private static final String GITHUB_GIT_CLONE_BASE_URL = "https://github.com";//GitHub git clone请求头部
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";//GitHub API头部
    private static final String ORG_NAME = "701Enti";//在Github的组织名


    /**
     * 列出组织的所有存储库信息
     * @param tag 调用本方法的方法的名字
     * @return 请求Github API获得的响应实体数据
     */
    public static ResponseEntity<String> listAllRemoteRepositoryInformation(@Nonnull String tag){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/vnd.github+json");
        headers.set("User-Agent","701Enti-website-backend-"+tag);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                GITHUB_API_BASE_URL+"/orgs"+"/"+ORG_NAME+"/repos",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }
}
