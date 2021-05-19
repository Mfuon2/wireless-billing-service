export interface Content {
    version: number;
    id: number;
    billNumber: string;
    billAmount: string;
    customerRefNumber: string;
    bankReference: string;
    tranParticular: string;
    paymentMode: string;
    transactionDate: string;
    phoneNumber: string;
    debitAccount: string;
    debitCustName: string;
    service: string;
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

export interface EquityResponseModel {
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

