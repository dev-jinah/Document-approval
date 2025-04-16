# Document Electronic Approval System / 문서 전자 결재 시스템

## Overview / 개요
This is a task to verify if it is possible to create a system according to the given requirements. / 주어진 요구 사항에 맞는 시스템을 만들 수 있는지 확인하기 위한 과제입니다.

The system to be built is a **Document Electronic Approval System**. / 만들어야 할 시스템은 **문서 전자 결재 시스템**입니다.

## Requirements / 요구사항

* Please create a fully functional web application or API server. / 완전히 동작하는 웹 어플리케이션 또는 API 서버를 작성해주세요.
    * If creating a web application, the web UI is not part of the evaluation. It is not necessary to apply CSS. / 웹 어플리케이션을 작성하는 경우 웹 UI는 평가대상이 아닙니다. CSS를 적용하지 않아도 됩니다.
    * If creating an API server, please write a script that shows the call order for typical use cases. / API 서버를 작성하는 경우 일반적인 사용 시나리오에 대한 호출 순서를 스크립트로 작성해주세요.
        * Example: Document approval: Call login API -> Call list API -> Call document view API -> Call document approval API / 예) 문서 결재: 로그인 API 호출 -> 목록 API 호출 -> 문서 보기 API 호출 -> 문서 승인 API 호출
* Please use a DBMS to permanently store the data. / 데이터를 영구히 저장하기 위해 DBMS를 사용해주세요.

## Feature Specifications / 기능 명세

### 1. User Model and Login System / 사용자 모델과 로그인 시스템
* A user model is required. / 사용자 모델이 필요합니다.
* The signup feature is not mandatory. / 가입 기능은 없어도 괜찮습니다.
* Users should be able to log in to access the system. / 사용자는 결재를 받기 위해 로그인해야 합니다.

### 2. Document Creation / 문서 생성
* Users should be able to create documents for approval. / 사용자는 결재받을 문서를 생성할 수 있어야 합니다.
    * The document will have a title, category, and content. / 문서는 제목, 분류, 내용으로 구성됩니다.
    * When creating a document, the user can specify the users they want to approve the document. / 문서 생성 시 결재를 해줄 사용자를 지정할 수 있습니다.
    * There can be more than one approver, and the creator of the document can also be an approver. / 결재자는 한명 이상이 될 수 있으며, 문서 작성자 본인도 지정할 수 있습니다.
    * Document approval should proceed in order. The second approver cannot approve before the first one. / 결재는 순서대로 진행됩니다. 두번째 결재자가 먼저 결재할 수는 없습니다.

### 3. Approval System / 결재 시스템
* A document is approved if all approvers approve it. If even one approver rejects the document, it will be rejected. / 문서는 모든 결재자가 승인하면 승인됩니다. 한명이라도 거절하면 거절됩니다.
* When approving or rejecting a document, approvers should be able to add comments or reasons. / 문서 승인/거절 시 결재자는 의견이나 사유를 추가할 수 있습니다.

### 4. Document Status and Listing / 문서 상태 및 목록
* Users should be able to view the following document lists: / 사용자는 다음과 같은 문서 목록을 볼 수 있어야 합니다:
    * **OUTBOX**: Documents I have created and are currently in the approval process. / **OUTBOX**: 내가 생성한 문서 중 결재 진행 중인 문서
    * **INBOX**: Documents that require my approval. / **INBOX**: 내가 결재를 해야 할 문서
    * **ARCHIVE**: Documents that I have been involved in, and their approval status (either approved or rejected). / **ARCHIVE**: 내가 관여한 문서 중 결재가 완료(승인 또는 거절)된 문서

---

## Technical Requirements / 기술 요구사항

* A DBMS must be used to store data permanently. / 데이터를 영구히 저장하기 위해 DBMS를 사용해야 합니다.
* The system should be built as either a web application or an API server. / 시스템은 웹 어플리케이션 또는 API 서버로 구축해야 합니다.
