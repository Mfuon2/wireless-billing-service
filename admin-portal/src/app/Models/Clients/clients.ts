export interface Content {
    accountNumber: string;
    msisdn: string;
    accountName: string;
    shortCode: string;
    emailAddress: string;
    serviceType: string;
    createdAt: string;
    updatedAt: string;
}

export interface ClientContent {
    accountNumber: string;
    accountName: string;
}

export interface Sort {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
}

export interface Pageable {
    sort: Sort;
    offset: number;
    pageNumber: number;
    pageSize: number;
    unpaged: boolean;
    paged: boolean;
}

export interface ClientsResponseDto {
    content: Content[];
    pageable: Pageable;
    totalPages: number;
    totalElements: number;
    last: boolean;
    size: number;
    number: number;
    sort: Sort;
    first: boolean;
    numberOfElements: number;
    empty: boolean;
}

export interface Data {
    accountNumber: string;
    msisdn: string;
    accountName: string;
    shortCode: string;
    emailAddress: string;
    serviceType: string;
    createdAt: string;
    updatedAt: string;
}

export interface ClientCreationModel {
    success: boolean;
    msg: string;
    data: Data;
}



