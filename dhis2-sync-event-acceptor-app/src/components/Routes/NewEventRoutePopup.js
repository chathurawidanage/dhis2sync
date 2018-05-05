import React from "react";
import {Dialog} from "@blueprintjs/core";
import axios from "axios";
import {getUrl} from "../../Constants";
import {showErrorToast, showSuccessToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import ButtonWithSpinner from "../common/ButtonWithSpinner";
import "./NewEventRoutePopup.css";
import ProgramStageSelector from "./ProgramStageSelector";

export default class NewEventRoutePopup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            sourceInstanceId: undefined,
            sourceProgramStage: undefined,
            destinationInstanceId: undefined,
            destinationProgramStage: undefined,
            savingInProgress: false
        }
    }


    setSavingInProgress = (inProgress) => {
        this.setState({
            savingInProgress: inProgress
        })
    };

    onSaveClicked = () => {
        this.setSavingInProgress(true);
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


    render() {
        return (
            <Dialog isOpen={this.props.isOpen} title="Create Event Route" onClose={this.props.onClose}>
                <div style={{padding: 10}}>
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