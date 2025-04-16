# **문서 전자 결재 시스템 / Document Electronic Approval System**


## **프로젝트 개요 / Project Overview**

이 프로젝트는 **문서 전자 결재 시스템**을 구축하는 것입니다. 시스템은 사용자가 문서를 생성하고, 결재를 요청하며, 결재가 완료된 문서를 관리하는 기능을 제공합니다.  
This project aims to build a **Document Electronic Approval System**. The system allows users to create documents, request approval, and manage documents that have been approved or rejected. It includes features for document creation, approval workflow, and viewing document statuses.


## **주요 기능 / Key Features**

### 1. **사용자 모델과 로그인 시스템 / User Model and Login System**
   - 사용자 모델을 정의하고, 사용자는 시스템에 로그인하여 문서를 생성하고 결재할 수 있어야 합니다.  
   - Define a user model, and users should be able to log in to access the system in order to create and approve documents.

### 2. **문서 생성 / Document Creation**
   - 사용자는 결재를 받을 문서를 생성할 수 있으며, 문서에는 제목, 분류, 내용이 포함됩니다.  
   - When creating a document, users should be able to specify other users to approve it.  
   - 결재자는 여러 명일 수 있으며, 문서 작성자도 결재자로 포함할 수 있습니다.  
   - There can be multiple approvers, and the document creator can also be an approver.  
   - 문서 결재는 순차적으로 진행됩니다. 두 번째 결재자가 첫 번째 결재자를 기다려야 합니다.  
   - Document approval must proceed in order. The second approver cannot approve before the first one.

### 3. **결재 시스템 / Approval System**
   - 모든 결재자가 승인을 해야 문서가 승인됩니다. 한 명이라도 거절하면 문서는 거절됩니다.  
   - A document is considered approved only when all approvers approve it. If even one approver rejects the document, it will be rejected.  
   - 결재자는 문서를 승인하거나 거절할 때 의견이나 사유를 추가할 수 있습니다.  
   - Approvers can add comments or reasons when approving or rejecting a document.

### 4. **문서 상태 및 목록 보기 / Document Status and Listing**
   - 사용자들은 자신이 생성한 문서나 승인해야 할 문서의 목록을 볼 수 있어야 합니다.  
   - Users should be able to view document lists based on their involvement:  
     - **OUTBOX**: 내가 생성하고 결재 진행 중인 문서 (Documents I have created and are in the approval process.)
     - **INBOX**: 내가 결재해야 할 문서 (Documents that require my approval.)
     - **ARCHIVE**: 내가 참여했던 문서 중 승인 또는 거절된 문서 (Documents I have been involved in, and their approval status (either approved or rejected).)


## **기술 요구 사항 / Technical Requirements**

- 데이터를 영구히 저장하기 위해 데이터베이스(DBMS)를 사용해야 합니다.  
  A DBMS must be used to store data permanently.  
- 시스템은 웹 애플리케이션 또는 API 서버 형태로 구현할 수 있습니다.  
  The system should be built as either a web application or an API server.


이 시스템을 통해 사용자는 효율적인 문서 결재 과정과 기록 관리가 가능하게 됩니다. 시스템의 사용 흐름은 직관적이며, 문서의 승인/거절과 관련된 중요한 정보를 관리하고 추적할 수 있는 기능이 제공됩니다.  
This system will allow users to efficiently manage the document approval process and track important approval/rejection information. The user flow is intuitive, and it provides essential features for managing and monitoring document statuses.
