import React from "react";
import Conference from "./Conference";


function ConferenceList() {
    const [conferences, setConferences] = React.useState([]);

    React.useEffect(() => {
        fetch('/api/active')
        .then(data =>  data.json())
        .then(conferences => setConferences(conferences))
    }, []);
    return(
        <div className="conference">
            {conferences.map(conference => <Conference key={conference.name} conference={conference}/>)}
        </div>
    );
}

export default ConferenceList;