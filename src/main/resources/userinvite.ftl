
<html>
<head>
    <title>Invitation</title>
    <meta charset="UTF-8">
    <meta name="description" content="" />
    <meta name="keywords" content="" />
    <link rel="stylesheet" href="border.css">    
</head>
<style>
.button {
  background-color: rgb(71, 151, 212);
  border: none;
  color: white;
  padding: 10px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  width: 249px;
  
}
.button4 {border-radius: 12px;}

.logo{

}
h1,h4{
    font-family: "berkshire-swash";
    color: rgb(71, 151, 212);
}
.frame{ 
    position: relative;
    padding: 10px;
  margin-left: 234px;
    font-family: ;
    color: rgba(0, 0, 0, 0.56);
    text-shadow: 0px 0px 5px rgba(0,0,0,0.2);
    
   margin-bottom: -13px;

    background-color: rgba(0, 0, 0, 0);
}
.frame1{ 
    position: relative;
    padding: 10px;
    text-align: center;
    font-family: ;
    color: rgba(0, 0, 0, 0.56);
    text-shadow: 0px 0px 5px rgba(0,0,0,0.2);
    margin: auto;


    background-color: rgba(0, 0, 0, 0);
}

</style>
<body>
    <div class="frame1">
        <h1>SYNECTIKS</h1>
        <h2>You've been invited to join Synectiks</h2>
	</div>
	<div class="frame">
		<h3>${ownerName}</h3> <p>
			Has invited you to join Synectiks. Please click on the button given below to accept the invitation<br>
	</div>
	<div class="frame1">
		<a class="button button4" href="${inviteLink}" target="_blank">Accept invitation</a>
    </div>
</body>
</html>
