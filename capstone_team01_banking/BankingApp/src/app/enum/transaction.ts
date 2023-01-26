export interface Transaction {
    transactionId: string;
    amount: number;
    dateCreated: string;
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