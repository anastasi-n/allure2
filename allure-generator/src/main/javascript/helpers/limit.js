export default function(string, limit) {
    return string.length > limit ? string.substring(0, limit - 3) + '...': string;
}
