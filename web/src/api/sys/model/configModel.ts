export interface GetSystemConfigModel {
    companyName: string;
    companyContact: string;
    companyAddress: string;
    companyPhone: string;
    companyFax: string;
    companyPostCode: string;
    companyLogo: string;
}

export interface AddOrUpdateSystemConfigModel {
    id: number | string;
    companyName: string;
    companyContact: string;
    companyAddress: string;
    companyPhone: string;
    companyFax: string;
    companyPostCode: string;
    companyLogo: string;
}