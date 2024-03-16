import React from "react";
import DataGroup from "./DataGroup";
import '../styles/DataGroup.css';
import axios from "axios";


function DataGroupsList() {

    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const [dataGroups, setDataGroups] = React.useState([]);


    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }

    React.useEffect(() => {
        axios.get(`/api/dataGroup/${user.conferenceId}`,
                    config)
        .then(response =>  response.data)
        .then(dataGroups => setDataGroups(dataGroups))
    }, []);

    return(
        <div className="dataGroup">
            {dataGroups.map(dataGroup => <DataGroup key={dataGroup.idDataGroup} dataGroup={dataGroup}/>)}
        </div>
    );
}

export default DataGroupsList;