export interface Content {
    version: number;
    id: number;
    callbackType: string;
    transactionType: string;
    transactionID: string;
    transTime: string;
    transAmount: string;
    businessShortCode: number;
    billRefNumber: string;
    invoiceNumber: string;
    orgAccountBalance: string;
    thirdPartyTransID: string;
    msisdn: string;
    firstName: string;
    middleName: string;
    lastName: string;
    createdAt: Date;
    updatedAt: Date;
}

export interface Sort {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
}

export interface Pageable {
    page: number;
    size: number;
    sort: string[];
}

export interface C2BResponseModel {
    totalPages: number;
    totalElements: number;
    size: number;
    content: Content[];
    number: number;
    sort: Sort;
    first: boolean;
    pageable: Pageable;
    numberOfElements: number;
    last: boolean;
    empty: boolean;
}

