import React from "react";
import {Icon, Switch, Intent} from "@blueprintjs/core";
import axios from "axios";
import {getUrl} from "../../Constants";
import {AppToaster} from "../../App";

export default class DHIS2InstanceDataElements extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            syncDataElements: [],
            d2iDataElementsMap: this.createD2iDataElementsMap()
        }
    }

    createD2iDataElementsMap = (d2iDataElements = this.props.dataElements) => {
        let deMap = {};
        d2iDataElements.forEach(d2iDataElement => {
            deMap[d2iDataElement.identifier] = d2iDataElement;
        });
        return deMap;
    };

    componentDidMount() {
        this.refreshDataElements();
    }

    refreshDataElements = () => {
        axios.get(`${getUrl('syncDataElements')}`)
            .then(response => {
                this.setState({
                    syncDataElements: response.data
                })
            })
            .catch(err => {
                console.error(err);
            })
    };

    changeSyncStatus = () => {

    };

    onChangeMapping = (d2iDataElementIdentifier, value) => {
        axios.post(`${getUrl(`d2iDataElements/${d2iDataElementIdentifier}/${value}`)}`)
            .then(response => {
                let d2iDataElement = response.data;
                this.setState({
                    d2iDataElementsMap: {...this.state.d2iDataElementsMap, [d2iDataElement.identifier]: d2iDataElement}
                });
                AppToaster.show({
                    message: `Successfully mapped ${d2iDataElement.displayName} to ${value}`,
                    intent: Intent.SUCCESS
                });
            })
            .catch(err => {
                console.error("Error occurred when doing the mapping", err);
                AppToaster.show({
                    message: `Failed to map data element : ${err.message}`,
                    intent: Intent.DANGER
                });
            });
    };

    render() {
        return (

            <table
                className="pt-html-table pt-html-table-striped" width="100%">
                <thead>
                <tr>
                    <td>Name</td>
                    <td className="text-center">Allowed By Source</td>
                    <td>Enable Syncing</td>
                    <td>Mapping</td>
                    <td className="text-center">Sync Status</td>
                </tr>
                </thead>
                <tbody>
                {this.state.d2iDataElementsMap && Object.values(this.state.d2iDataElementsMap).sort((de1, de2) => {
                    if (de1.displayName < de2.displayName)
                        return -1;
                    if (de1.displayName > de2.displayName)
                        return 1;
                    return 0;
                }).map(de => {
                    return <tr key={de.identifier}>
                        <td>{de.displayName}</td>
                        <td className="text-center">
                            {de.syncability.enabledBySource && <Icon icon="tick-circle" intent={Intent.SUCCESS}/>}
                            {!de.syncability.enabledBySource && <Icon icon="ban-circle" intent={Intent.DANGER}/>}
                        </td>
                        <td>
                            <Switch checked={de.syncability.enabledByServer}
                                    onChange={this.changeSyncStatus}/>
                        </td>
                        <td>
                            <div className="pt-select pt-fill">
                                <select value={(de.syncDataElement && de.syncDataElement.code) || ''}
                                        onChange={(event) => {
                                            this.onChangeMapping(de.identifier, event.target.value)
                                        }}>
                                    <option>NO MAPPING</option>
                                    {this.state.syncDataElements.map(sde => {
                                        return <option value={sde.code} key={sde.code}>{sde.displayName}</option>
                                    })}
                                </select>
                            </div>
                        </td>
                        <td className="text-center">
                            {de.syncability.enabled && de.syncDataElement &&
                            <Icon icon="tick-circle" intent={Intent.SUCCESS}/>}
                            {!(de.syncability.enabled && de.syncDataElement) &&
                            <Icon icon="ban-circle" intent={Intent.DANGER}/>}
                        </td>
                    </tr>
                })}
                </tbody>
            </table>
        )
    }
}
