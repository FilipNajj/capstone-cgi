export interface Transaction {
    transactionId: string;
    amount: number;
    dateCreated: Date;
    transactionType: string;
    clientId: number;
    adminId: number;
    transactionMedium: string;
    fromAccountNumber: number;
    toAccountNumber: number;
    emailRecipient: string;
    institutionId: number;
    createdBy: string;
}