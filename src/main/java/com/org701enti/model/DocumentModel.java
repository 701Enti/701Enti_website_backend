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
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class DocumentModel {

    //文档状态
    //所有状态只能由DocumentService的sync方法写入,其他实例都只能读取状态
    public enum DocumentStatus {
        AVAILABLE,//可用,文档文件和信息完全就绪,该状态下{允许用户访问文件},接收所有操作请求,等待更新等其他事件触发
        WAIT_FOR_CREATE,//等待创建,文档正在等待被系统创建,该状态下{禁止用户访问文件},可进行介入操作:[推迟创建(需要先删除文件),进入POSTPONE_CREATE],否则默认继续创建,完成后,进入AVAILABLE
        WAIT_FOR_UPDATE,//等待更新,远程仓库事件或用户的上传等使得系统发现了信息或文件更新,进入等待更新状态,该状态下{允许用户访问文件},可进行介入操作:[推迟更新(需要先撤销所有操作),进入POSTPONE_UPDATE],否则默认继续更新,完成后,进入AVAILABLE
        WAIT_FOR_GO_OFFLINE,//等待下线,远程仓库被删除等或用户的操作使得系统明确文档不再需要提供,进行下线处理,该状态下{允许用户访问文件},可进行介入操作:[推迟下线(需要先撤销所有操作),进入POSTPONE_GO_OFFLINE],否则默认继续下线,完成后,进入IGNORED_WITH_OFFLINE
        POSTPONE_CREATE,//推迟创建,该状态下{禁止用户访问文件},一直保持这个状态,除非进行介入操作:[继续创建,进入WAIT_FOR_CREATE] [忽略创建,直接设置IGNORED_WITH_OFFLINE,无需其他操作]
        POSTPONE_UPDATE,//推迟更新,该状态下{允许用户访问文件},一直保持这个状态,除非进行介入操作:[继续更新,进入WAIT_FOR_UPDATE] [忽略更新,直接设置IGNORED_WITH_SUPPORT,无需其他操作]
        POSTPONE_GO_OFFLINE,//推迟下线,该状态下{允许用户访问文件},一直保持这个状态,除非进行介入操作:[继续下线,进入WAIT_FOR_GO_OFFLINE] [取消下线,需要执行对应专门恢复程序来更新信息和[更新文件]到最新(或者检查更新确实最新那就不需要)(因为长时间推迟,文档的信息可能过时,更新时容易发生错误),从而回到AVAILABLE]
        IGNORED_WITH_SUPPORT,//已忽略,但是保持文档的支持(但是不会更新,可能文档一直是旧的),该状态下{允许用户访问文件},[忽略任何文档的更新,下线等操作(即使明确被触发)],除非执行对应专门恢复程序来更新信息和[更新文件]到最新(或者检查更新确实最新那就不需要)(因为长时间忽略,文档的信息可能过时,更新时容易发生错误),从而进入POSTPONE_GO_OFFLINE
        IGNORED_WITH_OFFLINE,//已忽略,并且已经下线文档(保留记录,删除文件),该状态下{禁止用户访问文件},[忽略任何文档的创建,更新,下线等操作(即使明确被触发)],除非执行对应专门恢复程序来更新信息和[下载文件]到最新(必须这样,因为文件之前删除了)(因为长时间忽略,文档的信息可能过时,更新时容易发生错误),从而进入IGNORED_WITH_SUPPORT
        STATUS_NOT_SET//状态没有被设置(数据库字段默认值),该状态下{禁止用户访问文件}
    }

    @Entity
    public static class DocumentMainInformation {
        @Setter
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(updatable = false)
        private Long id;//自增ID

        @Setter
        @Column(nullable = false,length = 50)
        private String name;//文档名

        @Setter
        @Column(nullable = false,length = 100,unique = true)
        private String uuid;//唯一标识,对于文档项目,为所在的Github存储库的全局节点ID或者其他平台的标识(如果存储库不是Github上的),对于离散的文档文件,为其SHA-256的16进制表示

        @Setter
        @Column(length = 50)
        private String aim;//对应项目名称(可以为空),仅用于网站数据关联,如701Enti的智能时钟项目的项目文档DOC-SEVETEST30对应aim字段为 SEVETEST30

        @Setter
        @Enumerated(EnumType.STRING)
        private DocumentStatus documentStatus;//文档状态,DocumentStatus一个枚举类型(映射见定义)

        @Setter
        @Column(nullable = false,length = 100)
        private String author;//作者/作者名单(如果可能有很多个作者建议使用组织名或合适的团体称呼),如果文档目录中有README文档,会尝试获取其中"author"注释里的100以内字符作为作者,找不到README文档或有README文档但没有"author"注释,设置为佚名即author=unknown

        @Setter
        @Column(length = 50)
        private String uploaderUserId;//上传者用户ID(可以为空,如果是系统自动托管而不是用户主动传输的,必须设置为空),格式为 授权平台 + ":" +上传用户在该平台 的唯一标识,如使用Github登录,格式为 Github:{此处为Github用户id}

        @Setter
        @Column(length = 100)
        private String briefDescription;//对文档的简介,为用户上传时输入或者远程仓库如github仓库的description

        @Setter
        @Column(nullable = false,updatable = false)
        private LocalDateTime createTime;//文档创建时间,为开始文档创建时的数据

        @Setter
        @Column(nullable = false)
        private LocalDateTime updateTimeContent;//文档内容最新更新时间,是指要更新的文档传输到网站,并完成更新,直接面向用户的时间(文档文件内容的更新)

        @Setter
        @Column(nullable = false)
        private LocalDateTime updateTimeEntity;//文档对应的数据库实体最新更新时间(修改数据库字段值也算实体更新)

        @Setter
        @Column(nullable = false,length = 512)
        private String root;//目录路径,如果是离散的文档文件,root指向其所在的目录,如果是HTML整合型文档项目,root指向index.html文件所在的目录

        @Setter
        @Column(length = 255)
        private String file;//具体指向的文件名(含扩展名)(可以为空,如果为空,指向的文件名为index.html),如果是离散的文档文件,为其含扩展名的文件名称,如果是HTML整合型文档项目,设置为空,或者指定index.html的同类文件

        @Setter
        @Column(length = 512)
        private String logoURL;//文档logo的URL(可以为空),如果未设置或设置为空,将使用默认的logo

        public DocumentMainInformation(){
            this.documentStatus = DocumentStatus.STATUS_NOT_SET;
        }


        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAim() {
            return aim;
        }

        public String getUuid() {
            return uuid;
        }

        public DocumentStatus getDocumentStatus() {
            return documentStatus;
        }

        public String getUploaderUserId() {
            return uploaderUserId;
        }

        public String getAuthor() {
            return author;
        }

        public String getBriefDescription() {
            return briefDescription;
        }

        public LocalDateTime getUpdateTimeContent() {
            return updateTimeContent;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public LocalDateTime getUpdateTimeEntity() {
            return updateTimeEntity;
        }

        public String getRoot() {
            return root;
        }

        public String getFile() {
            return file;
        }

        public String getLogoURL() {
            return logoURL;
        }

        //自动设置文档对应的数据库实体最新更新时间updateTimeEntity
        @PreUpdate
        protected void onUpdate(){
            this.updateTimeEntity = LocalDateTime.now();
        }
    }
}
