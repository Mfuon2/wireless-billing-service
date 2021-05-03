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

export interface Data {
    id: number;
    clientAccount: ClientAccount;
    servicePackage: ServicePackage;
    subscriptionPlan: string;
    createdAt: string;
    updatedAt: string;
}

export interface SubscriptionResponseModel {
    success: boolean;
    msg: string;
    data: Data;
}
