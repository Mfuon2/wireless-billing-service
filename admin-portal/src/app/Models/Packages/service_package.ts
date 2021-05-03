export interface Content {
    id: number;
    name: string;
    code: string;
    description: string;
    price: number;
    cycle: string;
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

export interface ServicesResponseDto {
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

export interface Data {
    id: number;
    name: string;
    code: string;
    description: string;
    price: number;
    cycle: string;
    createdAt: Date;
    updatedAt: Date;
}

export interface ServicePackageResponseModel {
    success: boolean;
    msg: string;
    data: Data;
}
