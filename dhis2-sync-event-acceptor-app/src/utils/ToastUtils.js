import {Intent} from "@blueprintjs/core";
/**
 * @author Chathura Widanage
 */
import {AppToaster} from "../App";

export const showErrorToast = (msg) => {
    AppToaster.show({
        message: msg,
        intent: Intent.DANGER
    })
};

export const showSuccessToast = (msg) => {
    AppToaster.show({
        message: msg,
        intent: Intent.SUCCESS
    })
};