import React from 'react';
import { Button, Form, Grid, Header, Segment } from 'semantic-ui-react';
import { ImImages } from "react-icons/im";
import { Link} from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import axios from 'axios';


export default function Login() {

  const [date, setDate] = React.useState("");
  const [imageSelected, setImageSelected] = React.useState("");

  const history = useHistory(); 

  const user = JSON.parse(localStorage.getItem('user'));
  const token = JSON.parse(localStorage.getItem('token'));

  const routeChange = function() {
    history.push('/multimedia');
  }

  const pConfig = {
    headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
  };

  /*async function onSubmit(e) {
    e.preventDefault();
    const data = {
        url: "",
        date: date
    };

    await axios.post(
        `multimedia/add/${user.idConference}`,
         data,
         pConfig
      ).then(response => console.log(response));

      routeChange();
  } */

  const upload = () => {
    const formData = new FormData();
    formData.append("file", imageSelected);
    formData.append("upload_preset", "rndmbu2h");

    axios.post(
        "https://api.cloudinary.com/v1_1/dgkmcrdrh/image/upload",
        formData
    ).then((response) => {
        const data = {
            date: date,
            url: response.data.secure_url
        }

        console.log(data);

        const pConfig = {
            headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
        };
        axios.post( 
            `/api/multimedia/add/${user.conferenceId}`,
             JSON.stringify(data),
             pConfig,
        ).then(response => {if(response.status===200) routeChange()}
        ).catch(function(error) {
          alert("Invalid date. Please try again.");
        });
    });
}

 return (
    <div>
    <div style={{width: "250px", margin: "10px"}}>
      <Link to='/myConference'>
        <Button color='teal' fluid size='large'>
          Go Back To My Conference
        </Button>
      </Link>              
    </div>
    <Grid textAlign='center' style={{ height: '100vh' }} verticalAlign='middle'>
    <Grid.Column style={{ maxWidth: 450 }}>
      <Header as='h2' color='teal' textAlign='center'>
        <div>
        <ImImages style={{ fontSize: '70px'}}/>
        </div>
        Create new multimedia folder
      </Header>
      <Form size='large'>
        <Segment stacked>
          <Form.Input type='text' onFocus={(e) => (e.currentTarget.type = "date")} fluid name = 'date' placeholder='Date'  onChange={({ target }) => setDate(target.value)} defaultValue={date} required/>
          <input type="file" onChange={(event) => {setImageSelected(event.target.files[0])}} required/>
          <Button color = 'teal' style={{marginLeft: 15,marginTop:15}} onClick={upload}>UPLOAD</Button>
        </Segment>
      </Form>
      </Grid.Column>
    </Grid>
  </div>
  );
} 