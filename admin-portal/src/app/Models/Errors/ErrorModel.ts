export interface SubError {
    objectName: string;
    field: string;
    rejectedValue: string;
    message: string;
}

export interface ErrorModel {
    status: any;
    message: string;
    debugMessage?: any;
    subErrors: SubError[];
    timestamp: string;
}

