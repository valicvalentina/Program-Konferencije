import React from 'react';
import '../styles/DataGroupPopUp.css';
import {FaFileUpload} from 'react-icons/fa';
import { Grid } from 'semantic-ui-react';
import axios from 'axios';
import {ImageUpload} from 'cloudinary-react';
import { Button } from 'semantic-ui-react';

  
export default function DataGroupPopup(){

  const [form, setForm] = React.useState({location:'' });
  const [conference, setConference] = React.useState([]);
  const [dataGroups, setDataGroups] = React.useState([]);
  const [schedule, setSchedule] = React.useState([]);
  const [collectionOfPapers, setCollectionsOfPapers] = React.useState([]);
  const [presentation, setPresentation] = React.useState([]);


  var user = JSON.parse(localStorage.getItem('user'));
  const token = JSON.parse(localStorage.getItem('token'));

  const pConfig = {
    headers: { method: 'POST', Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
  };
  const config= {
    headers: { Authorization : `Bearer ${token.token}`}
}


    React.useEffect(() => {
      const confId = async() => { 
        await axios.get(`/api/dataGroup/${user.conferenceId}`, 
                    config)
      .then(reponse =>  {return reponse.data})
      .then(dataGroups => setDataGroups(dataGroups))
    }

    
    confId();

    }, []);
  

  function onChange(event) {
    const {name, value} = event.target;
    console.log(name , value)
    console.log(form)
    setForm(oldForm => ({...oldForm, [name]: value}))
   }

   function getId(name){
      var wantedId;
      dataGroups.forEach(element => {
        if (element.groupName === name){
          wantedId = element.idDataGroup;
        }
      });
      return wantedId
     }

     function onSubmit(event) {
      console.log('Submitano je')
      event.preventDefault();
  
      if(schedule !== null && form.location !== '' 
        && collectionOfPapers !== null && presentation !== null) {
  
          //PRVO SLANJE RASPOREDA
        let idSchedule = getId('Raspored predavanja')
  
        const formData = new FormData();
        formData.append("file", schedule);
        formData.append("upload_preset", "loee137k");
  
        const firstFetch = axios.post(
          "https://api.cloudinary.com/v1_1/dxsl1srj1/image/upload",
          formData
          ).then((response) => {
            const dataSchedule = {
            idDataGroup: idSchedule, 
            GroupName: 'Raspored predavanja',
            data: response.data.secure_url,
          }
            axios.post( 
              `/api/dataGroup/update`,
               JSON.stringify(dataSchedule),
               pConfig,
            ).then(response => {console.log(dataSchedule)})
            .catch(err => alert('An error has occured'));
      });
      
  
      let idCollectionOfPapers = getId('Zbornik radova');
  
      const formDataPapers = new FormData();
      formDataPapers.append("file", collectionOfPapers);
      formDataPapers.append("upload_preset", "loee137k");
      console.log(formDataPapers)
  
  
      const secondFetch = axios.post(
        "https://api.cloudinary.com/v1_1/dxsl1srj1/image/upload",
        formDataPapers
        ).then((response) => {
        const dataCollectionOfPapers = {
          idDataGroup: idCollectionOfPapers,
          GroupName: 'Zbornik radova',
          data: response.data.secure_url,
        }
  
        axios.post( 
            `/api/dataGroup/update`,
             JSON.stringify(dataCollectionOfPapers),
             pConfig,
        ).then(response => {console.log(dataCollectionOfPapers)})
        .catch(err => alert('An error has occured'));
    });
  
  
      let idPresentation = getId('Prezentacije predavanja')
  
      
      const formDataPresentation = new FormData();
      formDataPresentation.append("file", collectionOfPapers);
      formDataPresentation.append("upload_preset", "loee137k");
      console.log(formDataPresentation)
  
  
      const thirdFetch = axios.post(
        "https://api.cloudinary.com/v1_1/dxsl1srj1/image/upload",
        formDataPresentation
    ).then((response) => {
        const dataPresentation = {
          idDataGroup: idPresentation,
          GroupName: 'Zbornik radova',
          data: response.data.secure_url,
        }
  
        axios.post( 
            `/api/dataGroup/update`,
             JSON.stringify(dataPresentation),
             pConfig,
        ).then(response => {console.log(dataPresentation)})
        .catch(err => alert('An error has occured'));
    });
  
    let idLocation = getId('Mjesto događanja')
  
      const dataLocation = {
        idDataGroup: idLocation,
        GroupName: 'Mjesto događanja',
        data: form.location,
    }
  
    const options = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(dataLocation)
     };
  
    const fourthFetch = axios.post('/api/dataGroup/update', JSON.stringify(dataLocation), pConfig);
  
  
          
          
    Promise.all([firstFetch, secondFetch, thirdFetch, fourthFetch])
    .then((responses) => {
      window.location.reload(true)
    })
    .catch((error) => {
      console.error(`Failed to fetch: ${error}`)
    });
          
        
  
  } else {
          alert("Please fill in all the mandatory information");
    }
  };
  


  return(
    <>
    <h1 style={{display:'inline-block'}}>Add mandatory data</h1>
          <div className="mt-5 md:mt-0">
            <form onSubmit={onSubmit} method="POST" className='form-template'>
              <div>
                <div className="form-template">
                <div>
                    <h3 className=" text-center block font-medium text-gray-700" >Schedule</h3>
                    <div className="mt-1 flex justify-center rounded-md border-2 border-dashed border-gray-300 px-6 pb-50">
                      <div className="space-y-1 text-center">
                        <div style={{ width:100, height:100, marginLeft:100, marginRight:100 }}>
                        <svg
                          className="mx-auto h-12 w-12 text-gray-400"
                          stroke="currentColor"
                          fill="none"
                          viewBox="0 0 48 48"
                          aria-hidden="true"
                        >
                          <path
                            d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                            strokeWidth={1}
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                        </svg>
                        </div>
                        <div className="flex text-sm text-gray-600">
                          <label
                            htmlFor="file-upload"
                            className="relative cursor-pointer rounded-mds font-medium text-indigo-600 focus-within:outline-none focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 hover:text-indigo-500" style={{margin: 10}}
                          >
                            
                            <input id="file-upload" name="schedule" type="file" placeholder='Please enter the link to the pdf file' onChange={(event) => {setSchedule(event.target.files[0]); console.log(event.target.files[0])}} />
                          </label>
                          <p className="pl-1">(or drag and drop)</p>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div>
                    <h3 className="text-center block text-sm font-medium text-gray-700">Collection of papers</h3>
                    <div className="mt-1 flex justify-center rounded-md border-2 border-dashed border-gray-300 px-6 pb-50">
                      <div className="space-y-1 text-center">
                        <div style={{ width:100, height:100, marginLeft:100, marginRight:100 }}>
                        <svg
                          className="mx-auto h-12 w-12 text-gray-400"
                          stroke="currentColor"
                          fill="none"
                          viewBox="0 0 48 48"
                          aria-hidden="true"
                        >
                          <path
                            d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                            strokeWidth={1}
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                        </svg>
                        </div>
                        <div className="flex text-sm text-gray-600">
                          <label
                            htmlFor="file-upload"
                            className="relative cursor-pointer rounded-md font-medium text-indigo-600 focus-within:outline-none focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 hover:text-indigo-500" style={{margin: 10}}
                          >
                            
                            <input id="file-upload" name="collectionOfPapers" placeholder='Please enter the link to the pdf file' type="file" className="sr-only" onChange={(event) => {setCollectionsOfPapers(event.target.files[0]); console.log(event.target.files[0])}}/>
                          </label>
                          <p className="pl-1">(or drag and drop)</p>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div>
                    <h3 className="text-center block text-sm font-medium text-gray-700">Presentation</h3>
                    <div className="mt-1 flex justify-center rounded-md border-2 border-dashed border-gray-300 px-6 pb-50">
                      <div className="space-y-1 text-center">
                        <div style={{ width:100, height:100, marginLeft:100, marginRight:100 }}>
                        <svg
                          className="mx-auto h-12 w-12 text-gray-400"
                          stroke="currentColor"
                          fill="none"
                          viewBox="0 0 48 48"
                          aria-hidden="true"
                        >
                          <path
                            d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                            strokeWidth={1}
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />
                        </svg>
                        </div>
                        <div className="flex text-sm text-gray-600">
                          <label
                            htmlFor="file-upload"
                            className="relative cursor-pointer rounded-md font-medium text-indigo-600 focus-within:outline-none focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 hover:text-indigo-500" style={{margin: 10}}
                          >
                            
                            <input id="file-upload" name="presentation" placeholder='Please enter the link to the pdf file' type="file" className="sr-only" onChange={(event) => {setPresentation(event.target.files[0]); console.log(event.target.files[0])}} />
                          </label>
                          <p className="pl-1">(or drag and drop)</p>
                        </div>
                        
      
                      </div>
                    </div>
                  </div>
                  
                  <div className="bg-gray-50 px-4 py-3 text-right sm:px-6">
                    <label>
                      <h3 className="text-center block text-sm font-medium text-gray-700">Location: </h3>
                         <input name="location" type="text"  placeholder={'Location'} onChange={onChange}/>
                    </label>

                  </div>

                  <div>
                    <Button color='teal' fluid size='large' style={{marginLeft: 100, marginTop: 50, marginBottom: 50}}
                      type="submit"
                    >
                    Save
                    </Button>
                  </div>
                  
                <div>
              </div>
            </div>
          </div>
            </form>
          </div>
          </>
  )
};
