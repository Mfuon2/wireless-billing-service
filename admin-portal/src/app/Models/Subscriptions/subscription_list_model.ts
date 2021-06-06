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

export interface ServicePackage {
    id: number;
    name: string;
    code: string;
    description: string;
    price: number;
    cycle: string;
    createdAt: string;
    updatedAt: string;
}

export interface Content {
    id: number;
    clientAccount: ClientAccount;
    servicePackage: ServicePackage;
    subscriptionPlan: string;
    createdAt: string;
    updatedAt: string;
}

export interface SubscriptionD {
    accountNumber: string;
    serviceCode: string;
    subscriptionPlan: string;
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

export interface SubscriptionResponseDto {
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

