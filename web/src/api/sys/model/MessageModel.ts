export interface MessageInfo {
    key: string;
    name: string;
    list: ListItem[];
}

export interface ListItem {
    id: string;
    title: string;
    avatar: string;
    description: string;
    msgContent: string;
    type: string;
    status: string;
    datetime: string;
}

export interface ReadMessageReq {
    id: string;
    userId: string | number;
    status: number;
}