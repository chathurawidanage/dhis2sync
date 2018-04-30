/**
 * @author Chathura Widanage
 */
export const extractAxiosError = (err) => {
    return (err.response && err.response.data && err.response.data.message) || err.message;
};