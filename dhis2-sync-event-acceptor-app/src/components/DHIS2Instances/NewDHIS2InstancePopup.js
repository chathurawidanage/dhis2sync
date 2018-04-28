import React from "react";
import {Dialog, Button, FormGroup} from "@blueprintjs/core";
import axios from "axios";
import {getUrl, SERVER_URL} from "../../Constants";

export default class NewDHIS2InstancePopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            id: "",
            url: "",
            description: ""
        }
    }

    onPropertyChanged = (property, event) => {
        this.setState({
            [property]: event.target.value
        })
    };

    onSaveClicked = () => {
        axios.post(`${getUrl('dhis2Instances')}`, this.state)
            .then(response => {
                this.props.onInstanceAdded(response.data);
            })
            .catch(err => {
                console.error(err);
                this.props.onClose();
            })
    };

    render() {
        return (
            <Dialog isOpen={this.props.isOpen} title="New DHIS2 Instance" onClose={this.props.onClose}>
                <div style={{padding: 10}}>
                    <FormGroup
                        helperText="Instance Identifier"
                        label="ID"
                        labelFor="id-input"
                        requiredLabel={true}>
                        <input id="id-input" placeholder="DNMS" className="pt-input"
                               value={this.state.id}
                               onChange={(event) => {
                                   this.onPropertyChanged("id", event)
                               }}/>
                    </FormGroup>
                    <FormGroup
                        helperText="Instance URL"
                        label="Url"
                        labelFor="url-input"
                        requiredLabel={true}>
                        <input id="url-input" placeholder="https://myinstance.com/dhis2" className="pt-input"
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
                        <textarea id="desc-input" className="pt-input"
                                  value={this.state.description}
                                  onChange={(event) => {
                                      this.onPropertyChanged("description", event)
                                  }}/>
                    </FormGroup>
                    <Button text="Save" onClick={this.onSaveClicked}/>
                </div>
            </Dialog>
        )
    }
}