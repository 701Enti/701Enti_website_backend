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
//        SOFTWARE
package com.org701enti.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class DocumentModel {

    public enum DocumentStatus { //文档状态
        AVAILABLE,//可用,文档文件和信息完全就绪,该状态下允许用户访问,接收所有操作请求,等待更新等其他事件触发
        WAIT_FOR_CREATE,//等待创建,文档正在等待被系统创建,该状态下禁止用户访问,接下来仅允许放弃创建(设置状态为已忽略)或者推迟创建或者继续创建(一般是要执行git clone或者存储文档文件来继续完成创建)
        WAIT_FOR_UPDATE,//等待更新,远程仓库push或用户的上传等使得系统发现了新版本,进入等待更新状态,该状态下应该始终允许用户访问,接下来仅允许推迟更新或者继续更新(一般是要执行git pull或者存储新文档文件来继续完成更新)
        WAIT_FOR_GO_OFFLINE,//等待下线,远程仓库被删除等或用户的操作使得系统明确文档不再需要提供,进行下线处理,该状态下应该始终允许用户访问直到文件删除,同时需要明确告知用户文档即将下线,接下来仅允许执行下线程序或推迟下线,如果下线,进入IGNORED_WITH_OFFLINE
        POSTPONE_CREATE,//推迟创建
        POSTPONE_UPDATE,//推迟更新
        POSTPONE_GO_OFFLINE,//推迟下线
        IGNORED_WITH_SUPPORT,//已忽略,但是保持文档的支持,该状态下允许用户访问,忽略任何文档的更新,下线等操作(即使明确被触发),除非被重新设置为推迟下线,之后允许再次设置为可用
        IGNORED_WITH_OFFLINE//已忽略,并且已经下线文档(文件删除,停止所有访问支持,但是文档的数据库信息仍然),该状态下用户无法访问,忽略任何文档的更新,下线等操作(即使明确被触发),除非执行恢复程序,从而回到IGNORED_WITH_SUPPORT
    }

    @Entity
    public static class DocumentMainInformation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false)
        private Long id;//自增ID

        @Column(nullable = false,length = 50)
        private String name;//文档名

        @Column(nullable = false,length = 100,unique = true)
        private String uuid;//唯一标识,对于文档项目,为所在的Github存储库的全局节点ID或者其他平台的标识(如果存储库不是Github上的),对于离散的文档文件,为其SHA-256的16进制表示

        @Column(length = 50)
        private String aim;//对应项目名称(可以为空),仅用于网站数据关联,如701Enti的智能时钟项目的项目文档DOC-SEVETEST30对应aim字段为 SEVETEST30

        @Enumerated(EnumType.STRING)
        private DocumentStatus documentStatus;

        @Column(nullable = false,length = 100)
        private String author;//作者/作者名单(如果可能有很多个作者建议使用组织名或合适的团体称呼),如果文档目录中有README文档,会尝试获取其中"author"注释里的100以内字符作为作者,找不到README文档或有README文档但没有"author"注释,设置为佚名即author=unknown

        @Column(length = 50)
        private String uploaderUserId;//上传者用户ID(可以为空,如果是系统自动托管而不是用户主动传输的,必须设置为空),格式为 授权平台 + ":" +上传用户在该平台 的唯一标识,如使用Github登录,格式为 Github:{此处为Github用户id}

        @Column(nullable = false,length = 100)
        private String briefDescription;//对文档的简介,如果文档目录中有README文档,会使用其中前100个字符作为文档的简介,如果发现"BriefDescription"注释,会获取注释中100以内字符作为文档的简介

        @Column(nullable = false)
        private LocalDateTime updateTimeContent;//文档内容最新更新时间,是指要更新的文档传输到网站,并完成更新时的时间

        @Column(nullable = false,updatable = false)
        private LocalDateTime createTime;//文档创建时间,即首次在网站公开发布的时间,就是第一次发布操作的updateTimeContent

        @Column(nullable = false)
        private LocalDateTime updateTimeEntity;//文档对应的数据库实体最新更新时间

        @Column(nullable = false,length = 512)
        private String root;//目录路径,如果是离散的文档文件,为其所在的根目录,如果是HTML整合型文档项目,为其index.html文件所在的根目录

        @Column(length = 255)
        private String file;//具体指向的文件名(含扩展名)(可以为空,如果为空,指向的文件名为index.html),如果是离散的文档文件,为其含扩展名的文件名称,如果是HTML整合型文档项目,设置为空,或者指定index.html的同类文件

        @Column(length = 512)
        private String logoURL;//文档logo的URL(可以为空),如果未设置或设置为空,将使用默认的logo

        public DocumentMainInformation(){
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAim() {
            return aim;
        }

        public void setAim(String aim) {
            this.aim = aim;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public DocumentStatus getDocumentStatus() {
            return documentStatus;
        }

        public void setDocumentStatus(DocumentStatus documentStatus) {
            this.documentStatus = documentStatus;
        }

        public String getUploaderUserId() {
            return uploaderUserId;
        }

        public void setUploaderUserId(String uploaderUserId) {
            this.uploaderUserId = uploaderUserId;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getBriefDescription() {
            return briefDescription;
        }

        public void setBriefDescription(String briefDescription) {
            this.briefDescription = briefDescription;
        }

        public LocalDateTime getUpdateTimeContent() {
            return updateTimeContent;
        }

        public void setUpdateTimeContent(LocalDateTime updateTimeContent) {
            this.updateTimeContent = updateTimeContent;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }

        public LocalDateTime getUpdateTimeEntity() {
            return updateTimeEntity;
        }

        public void setUpdateTimeEntity(LocalDateTime updateTimeEntity) {
            this.updateTimeEntity = updateTimeEntity;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getLogoURL() {
            return logoURL;
        }

        public void setLogoURL(String logoURL) {
            this.logoURL = logoURL;
        }

        //自动设置文档对应的数据库实体最新更新时间updateTimeEntity
        @PreUpdate
        protected void onUpdate(){
            this.updateTimeEntity = LocalDateTime.now();
        }
    }
}
