import React from "react";



function Conference(props) {
    var user = JSON.parse(localStorage.getItem('user'));
    const {name, city, description, topics} = props.conference;
    return (
        <div>
        <div className="conference-list-element">
            <p style={{margin:12}}><b>{name}</b></p>
            <div className="conference-info">
                <p><span>City:</span> {city}</p>
                <p><span>About:</span> {description}</p>
                <div>
                        <>
                        <p><span>Topics:</span></p>
                        {String(topics).split(' ').map((t) => (
                            <p style={{marginLeft: 40, fontSize:19}}>{t}</p>
                        ))}
                        </>
                </div>
            </div>
        </div>
        </div>
    );
}

export default Conference;
