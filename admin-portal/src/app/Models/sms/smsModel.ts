export interface Content {
    id: number;
    msisdn: string;
    message: string;
    userName: string;
    senderId: string;
    attemptCount: number;
    attemptedTime: string;
    status: string;
    apiResponse: string;
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

export interface SmsLogModel {
    totalPages: number;
    totalElements: number;
    size: number;
    content: Content[];
    number: number;
    sort: Sort;
    pageable: Pageable;
    first: boolean;
    numberOfElements: number;
    last: boolean;
    empty: boolean;
}

export interface ResendSmsResponseModel {
    success: boolean;
    msg: string;
    data: string;
}



