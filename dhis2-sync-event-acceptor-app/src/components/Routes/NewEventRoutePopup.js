import React from "react";
import {Dialog} from "@blueprintjs/core";
import axios from "axios";
import {getUrl} from "../../Constants";
import {showErrorToast, showSuccessToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import ButtonWithSpinner from "../common/ButtonWithSpinner";
import "./NewEventRoutePopup.css";
import ProgramStageSelector from "./ProgramStageSelector";
import {FormGroup} from "@blueprintjs/core/lib/cjs/components/forms/formGroup";

export default class NewEventRoutePopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            sourceInstanceId: undefined,
            sourceProgramStage: undefined,
            destinationInstanceId: undefined,
            destinationProgramStage: undefined,
            savingInProgress: false,
            name: ''
        }
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.isOpen !== this.props.isOpen) {
            this.setState({
                sourceInstanceId: undefined,
                sourceProgramStage: undefined,
                destinationInstanceId: undefined,
                destinationProgramStage: undefined,
                name: ''
            });
        }
    }


    setSavingInProgress = (inProgress) => {
        this.setState({
            savingInProgress: inProgress
        })
    };

    onSaveClicked = () => {
        this.setSavingInProgress(true);
        axios.post(`${getUrl('eventRoutes')}`,
            {
                sourceProgramStage: this.state.sourceProgramStage,
                destinationProgramStage: this.state.destinationProgramStage,
                name: this.state.name
            })
            .then(response => {
                this.props.onRouteAdded(response.data);
                showSuccessToast(`Route created successfully`);
                this.setSavingInProgress(false)
            })
            .catch(err => {
                console.error(err);
                showErrorToast(`Failed to create route : ${extractAxiosError(err)}`);
                this.setSavingInProgress(false);
            })
    };

    onSourceInstanceChanged = (sourceInstanceId) => {
        this.setState({
            sourceInstanceId: sourceInstanceId,
            sourceProgramStage: this.state.sourceInstanceId !== sourceInstanceId ? undefined : this.state.sourceProgramStage
        });
    };

    onSourceProgramStageChanged = (programStageId) => {
        this.setState({
            sourceProgramStage: programStageId
        });
    };

    onDestinationInstanceChanged = (destinationInstanceId) => {
        this.setState({
            destinationInstanceId: destinationInstanceId,
            destinationProgramStage: this.state.destinationInstanceId !== destinationInstanceId ? undefined : this.state.destinationProgramStage
        });
    };

    onDestinationProgramStageChanged = (programStageId) => {
        this.setState({
            destinationProgramStage: programStageId
        });
    };

    onNameChange = (event) => {
        this.setState({
            name: event.target.value
        })
    };

    render() {
        return (
            <Dialog isOpen={this.props.isOpen} title="Create Event Route" onClose={this.props.onClose}>
                <div style={{padding: 10}}>
                    <FormGroup
                        label="Route Name"
                        labelFor="name-text-input">
                        <input id="name-text-input" placeholder="DNMS to NNIS risk events"
                               value={this.state.name}
                               className="pt-input pt-fill" onChange={this.onNameChange}/>
                    </FormGroup>
                    <ProgramStageSelector title="Source Program Stage"
                                          ignoreInstanceId={this.state.destinationInstanceId}
                                          onInstanceChanged={this.onSourceInstanceChanged}
                                          onProgramStageChanged={this.onSourceProgramStageChanged}
                                          selectedInstanceId={this.state.sourceInstanceId}/>

                    <ProgramStageSelector title="Destination Program Stage"
                                          ignoreInstanceId={this.state.sourceInstanceId}
                                          onInstanceChanged={this.onDestinationInstanceChanged}
                                          onProgramStageChanged={this.onDestinationProgramStageChanged}
                                          selectedInstanceId={this.state.destinationInstanceId}/>

                    <div className="text-right">
                        <ButtonWithSpinner
                            onClick={this.onSaveClicked}
                            disabled={this.state.savingInProgress || !(this.state.sourceProgramStage && this.state.destinationProgramStage)}
                            loading={this.state.savingInProgress}
                            text="Save"/>
                    </div>
                </div>
            </Dialog>
        )
    }
}