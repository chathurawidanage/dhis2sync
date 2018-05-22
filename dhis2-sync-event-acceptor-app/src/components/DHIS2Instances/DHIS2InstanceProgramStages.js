import React from "react";
import {Icon, Switch, Intent} from "@blueprintjs/core";

export default class DHIS2InstanceProgramStages extends React.Component {

    changeSyncStatus = () => {

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
                    <td className="text-center">Sync Status</td>
                </tr>
                </thead>
                <tbody>
                {this.props.programStages.map(ps => {
                    return <tr key={ps.identifier}>
                        <td>[{ps.dhis2InstanceProgram.displayName}] {ps.displayName}</td>
                        <td className="text-center">
                            {ps.syncability.enabledBySource && <Icon icon="tick-circle" intent={Intent.SUCCESS}/>}
                            {!ps.syncability.enabledBySource && <Icon icon="ban-circle" intent={Intent.DANGER}/>}
                        </td>
                        <td>
                            <Switch checked={ps.syncability.enabledByServer}
                                    onChange={this.changeSyncStatus}/>
                        </td>
                        <td className="text-center">
                            {ps.syncability.enabled && <Icon icon="tick-circle" intent={Intent.SUCCESS}/>}
                            {!ps.syncability.enabled && <Icon icon="ban-circle" intent={Intent.DANGER}/>}
                        </td>
                    </tr>
                })}
                </tbody>
            </table>
        )
    }
}
