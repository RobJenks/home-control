export type ResponseData = {
    count: int,
    data: Data[]
}

export type Data = {
    key: string,
    offset: int,
    value: Object[]
}