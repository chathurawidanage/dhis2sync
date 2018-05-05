import React from "react";
import {ControlGroup} from "@blueprintjs/core";
import {getUrl} from "../../Constants";
import {showErrorToast} from "../../utils/ToastUtils";
import {extractAxiosError} from "../../utils/AxiosUtils";
import axios from "axios";

export default class ProgramStageSelector extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            instances: {},
            instanceId: undefined
        }
    }

    componentDidMount() {
        this.refreshInstances();
    }

    refreshInstances = () => {
        axios.get(`${getUrl('dhis2Instances')}`)
            .then(response => {
                let instancesMap = {};
                response.data.forEach(instance => {
                    instancesMap[instance.id] = instance;
                });
                this.setState({
                    instances: instancesMap
                })
            })
            .catch(err => {
                console.error("Error in fetching dhis2 instances", err);
                showErrorToast(`Error in fetching dhis2 instances : ${extractAxiosError(err)}`)
            })
    };

    onInstanceChanged = (event) => {
        let instanceId = event.target.value;
        if (instanceId !== "NULL") {
            this.props.onInstanceChanged(event.target.value);
        } else {
            this.props.onInstanceChanged();
        }
    };

    onProgramStageChanged = (event) => {
        let programStageId = event.target.value;
        if (programStageId !== "NULL") {
            this.props.onProgramStageChanged(programStageId);
        } else {
            this.props.onProgramStageChanged();
        }
    };

    render() {
        return (
            <div className="program-stage-selector-wrapper">
                <h6>{this.props.title}</h6>
                <ControlGroup fill={true} vertical={false}>
                    <div className="pt-select pt-fill">
                        <select onChange={this.onInstanceChanged}>
                            <option value="NULL">Choose an Instance</option>
                            {this.state.instances && Object.values(this.state.instances).map(instance => {
                                if (instance.id === this.props.ignoreInstanceId) {
                                    return null;
                                } else {
                                    return <option key={instance.id} value={instance.id}>{instance.id}</option>
                                }
                            })}
                        </select>
                    </div>
                    <div className="pt-select pt-fill">
                        <select onChange={this.onProgramStageChanged}>
                            <option value="NULL">Choose a Program Stage</option>
                            {
                                this.props.selectedInstanceId
                                && this.state.instances
                                && this.state.instances[this.props.selectedInstanceId]
                                && this.state.instances[this.props.selectedInstanceId].programStages
                                && this.state.instances[this.props.selectedInstanceId].programStages.map(ps => {
                                    return <option key={ps.identifier}
                                                   value={ps.identifier}>{ps.displayName}</option>
                                })
                            }
                        </select>
                    </div>
                </ControlGroup>
            </div>
        )
    }
}