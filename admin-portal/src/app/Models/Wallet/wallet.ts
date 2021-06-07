export interface Content {
    id: number;
    serviceType: string;
    clientAccount: ClientAccount;
    balance: number;
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

export interface ClientAccount {
    accountNumber: string;
    msisdn: string;
    accountName: string;
    shortCode: string;
    emailAddress: string;
    serviceType: string;
    createdAt: string;
    updatedAt: string;
}
export interface WalletListingDto {
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

export interface Data {
    id: number;
    serviceType: string;
    balance: number;
    createdAt: Date;
    updatedAt: Date;
}

export interface WalletResponseModel {
    success: boolean;
    msg: string;
    data: Data;
}

export interface WalletRequestModel {
    accountNumber: string;
    serviceType: string;
    balance: number;
}


