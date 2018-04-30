import React from "react";
import {Dialog, Button, FormGroup} from "@blueprintjs/core";
import axios from "axios";
import {getUrl, SERVER_URL} from "../../Constants";
import {showErrorToast, showSuccessToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import {Spinner} from "@blueprintjs/core/lib/cjs/components/spinner/spinner";
import ButtonWithSpinner from "../common/ButtonWithSpinner";

export default class NewDHIS2InstancePopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: "",
            url: "",
            description: "",
            savingInProgress: false
        }
    }

    onPropertyChanged = (property, event) => {
        this.setState({
            [property]: event.target.value
        })
    };

    setSavingInProgress = (inProgress) => {
        this.setState({
            savingInProgress: inProgress
        })
    };

    onSaveClicked = () => {
        this.setSavingInProgress(true)
        axios.post(`${getUrl('dhis2Instances')}`,
            {
                id: this.state.id,
                url: this.state.url,
                description: this.state.description
            })
            .then(response => {
                this.props.onInstanceAdded(response.data);
                showSuccessToast(`Instance created successfully`);
                this.setSavingInProgress(false)
            })
            .catch(err => {
                console.error(err);
                showErrorToast(`Failed to create instance : ${extractAxiosError(err)}`);
                this.setSavingInProgress(false);
            })
    };

    render() {
        return (
            <Dialog isOpen={this.props.isOpen} title="Create DHIS2 Instance" onClose={this.props.onClose}>
                <div style={{padding: 10}}>
                    <FormGroup
                        helperText="Instance Identifier"
                        label="ID"
                        labelFor="id-input"
                        requiredLabel={true}>
                        <input id="id-input" placeholder="DNMS" className="pt-input pt-fill"
                               value={this.state.id}
                               onChange={(event) => {
                                   this.onPropertyChanged("id", event)
                               }}/>
                    </FormGroup>
                    <FormGroup
                        helperText="Instance URL"
                        label="URL"
                        labelFor="url-input"
                        requiredLabel={true}>
                        <input id="url-input" placeholder="https://myinstance.com/dhis2" className="pt-input pt-fill"
                               value={this.state.url}
                               onChange={(event) => {
                                   this.onPropertyChanged("url", event)
                               }}/>
                    </FormGroup>
                    <FormGroup
                        helperText="Short description about the instance"
                        label="Description"
                        labelFor="desc-input"
                        requiredLabel={true}>
                        <textarea id="desc-input" className="pt-input pt-fill"
                                  value={this.state.description}
                                  onChange={(event) => {
                                      this.onPropertyChanged("description", event)
                                  }}/>
                    </FormGroup>
                    <div className="text-right">
                        <ButtonWithSpinner
                            onClick={this.onSaveClicked}
                            disabled={this.state.savingInProgress}
                            loading={this.state.savingInProgress}
                            text="Save"/>
                    </div>
                </div>
            </Dialog>
        )
    }
}