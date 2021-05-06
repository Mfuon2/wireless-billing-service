export interface Content {
    id: number;
    shortCode: number;
    amount: number;
    msisdn: string;
    transactionType: string;
    transactionDescription: string;
    accountReference: string;
    transactionDate: string;
    merchantRequestId: string;
    checkoutRequestId: string;
    responseCode: number;
    responseDescription: string;
    customerMessage: string;
    resultCode?: any;
    resultDescription?: any;
    mpesaReceiptNumber?: any;
    serviceType: string;
    paymentStatus: string;
    serviceRequestStatus: string;
    servicePaymentStatus?: any;
    requestType: string;
    fullName: string;
    subscriptionPlan: string;
    createdAt: string;
    updatedAt: string;
}

export interface Sort {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
}

export interface Pageable {
    sort: Sort;
    offset: number;
    pageNumber: number;
    pageSize: number;
    unpaged: boolean;
    paged: boolean;
}

export interface ExpressListingModel {
    content: Content[];
    pageable: Pageable;
    totalPages: number;
    totalElements: number;
    last: boolean;
    size: number;
    number: number;
    sort: Sort;
    first: boolean;
    numberOfElements: number;
    empty: boolean;
}


// STATUS

export interface Data {
    id: number;
    shortCode: number;
    amount: number;
    msisdn: string;
    transactionType: string;
    transactionDescription: string;
    accountReference: string;
    transactionDate: string;
    merchantRequestId: string;
    checkoutRequestId: string;
    responseCode: number;
    responseDescription: string;
    customerMessage: string;
    resultCode: number;
    resultDescription: string;
    mpesaReceiptNumber?: any;
    serviceType: string;
    paymentStatus: string;
    serviceRequestStatus: string;
    servicePaymentStatus?: any;
    requestType: string;
    fullName: string;
    subscriptionPlan: string;
    createdAt: string;
    updatedAt: string;
}

export interface MpesaTransStatusModel {
    success: boolean;
    msg?: any;
    data: Data;
}


export interface StkPushRequestModel {
    fullName: string;
    customerPhoneNumber: string;
    accountReference: string;
    payableAmount: number;
    subscriptionPlan: string;
    serviceType: string;
    idNumber: string;
    description: string;
    transactionType: string;
}

// Stk Response
export interface Data {
    merchantRequestId: string;
    checkoutRequestId: string;
    responseDescription: string;
    responseCode: number;
    customerMessage: string;
}

export interface StkResponseModel {
    success: boolean;
    msg?: any;
    data: Data;
}

//STK Status
export interface Data {
    merchantRequestId: string;
    checkoutRequestId: string;
    responseCode: number;
    responseDescription: string;
    resultCode: number;
    resultDescription: string;
}

export interface TransactionStatusResponseModel {
    success: boolean;
    msg?: any;
    data: Data;
}
