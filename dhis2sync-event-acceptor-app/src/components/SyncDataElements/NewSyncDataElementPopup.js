import React from "react";
import {Dialog, Button, FormGroup} from "@blueprintjs/core";
import axios from "axios";
import {SERVER_URL} from "../../Constants";

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
            })
            .catch(err => {
                console.error(err);
                this.props.onClose();
            })
    };

    render() {
        return (
            <Dialog isOpen={this.props.isOpen} title="New Sync Data Element" onClose={this.props.onClose}>
                <div style={{padding: 10}}>
                    <FormGroup
                        helperText="Display name for the data element"
                        label="Name"
                        labelFor="text-input"
                        requiredLabel={true}>
                        <input id="name-input" placeholder="Name" className="pt-input"
                               value={this.state.displayName} onChange={this.onNameChanged}/>
                    </FormGroup>
                    <FormGroup
                        helperText="Unique Code for the data element"
                        label="Code"
                        labelFor="code-input"
                        requiredLabel={true}>
                        <input id="code-input" placeholder="Code" className="pt-input"
                               value={this.state.code} onChange={this.onCodeChanged}/>
                    </FormGroup>
                    <Button text="Save" onClick={this.onSaveClicked}/>
                </div>
            </Dialog>
        )
    }
}