import React from "react";
import {Dialog, Button, FormGroup, Intent} from "@blueprintjs/core";
import axios from "axios";
import {SERVER_URL} from "../../Constants";
import {AppToaster} from "../../App";
import {extractAxiosError} from "../../utils/AxiosUtils";
import {showErrorToast, showSuccessToast} from "../../utils/ToastUtils";

export default class NewSyncDataElementPopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            displayName: "",
            code: "",
        }
    }

    onNameChanged = (event) => {
        this.setState({
            displayName: event.target.value
        })
    };

    onCodeChanged = (event) => {
        this.setState({
            code: event.target.value
        })
    };

    onSaveClicked = () => {
        axios.post(`${SERVER_URL}syncDataElements`, this.state)
            .then(response => {
                this.props.onElementAdded();
                showSuccessToast("Data Element created");
            })
            .catch((err) => {
                console.error(err.response);
                showErrorToast(extractAxiosError(err));
            })
    };

    render() {
        return (
            <Dialog isOpen={this.props.isOpen} title="New Sync Data Element" onClose={this.props.onClose}>
                <div style={{padding: 10}}>
                    <FormGroup
                        helperText="Unique Code for the data element"
                        label="Code"
                        labelFor="code-input"
                        requiredLabel={true}>
                        <input id="code-input" placeholder="HEIGHT" className="pt-input pt-fill"
                               value={this.state.code} onChange={this.onCodeChanged}/>
                    </FormGroup>
                    <FormGroup
                        helperText="Display name for the data element"
                        label="Name"
                        labelFor="text-input"
                        requiredLabel={true}>
                        <input id="name-input" placeholder="Height" className="pt-input pt-fill"
                               value={this.state.displayName} onChange={this.onNameChanged}/>
                    </FormGroup>
                    <div className="text-right">
                        <Button text="Save" onClick={this.onSaveClicked}/>
                    </div>
                </div>
            </Dialog>
        )
    }
}