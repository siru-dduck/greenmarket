export const numberArrayParser = value => {
    value = value.split(",");
    return value.map(v => parseInt(v));
};