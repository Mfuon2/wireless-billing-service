export interface Content {
    id: number;
    voucherId: string,
    portal: string,
    plan: string,
    creationTime: string,
    claimedTime: string,
    expiryTime: string,
    createdAt: Date;
    updatedAt: Date;
}

export class CSVRecord {
    public id: any;
    public voucherId: any;
    public portal: any;
    public plan: any;
    public creationTime: any;
    public expiryTime: any;
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

export interface VouchersListingDto {
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

export interface ClaimModel {
    id: number;
    msisdn: string;
    reason: string;
    mpesaMessage: string;
    remarks: string;
}

export interface ClaimedResponse {
    success: boolean;
    msg: string;
    data?: any;
}









