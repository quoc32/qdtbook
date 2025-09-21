import React from "react";
import { useState, useEffect } from "react";
import { getDaysInMonth } from "date-fns";
import { useNavigate } from "react-router-dom";

import Announcement from "../components/Announcement";

export default function QDTRegister() {
  
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [gender, setGender] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const handleClickGender = (_gender) => {
    setGender(_gender);
  }
  const [year, setYear] = useState(2025);
  const [month, setMonth] = useState(1);
  const [day, setDay] = useState(1);
  const [days, setDays] = useState([]);
  const years = Array.from({ length: 2025 - 1905 + 1 }, (_, i) => 2025 - i);
  const months = Array.from({ length: 12 }, (_, i) => i + 1);
  const padZero = (num) => num.toString().padStart(2, "0");
  useEffect(() => {
    setDays(Array.from({ length: getDaysInMonth(new Date(year, month - 1)) }, (_, i) => i + 1));
  }, [year, month]);

  // >> Xử lý thông báo đăng ký thành công hay thất bại
  const [announcement, setAnnouncement] = useState(null);
  useEffect(() => {
    if (announcement) {
      const timer = setTimeout(() => {
        setAnnouncement(null)
      }, 3000) // Thời gian ô thông báo hiện ra là 3s
      return () => clearTimeout(timer)
    }
  }, [announcement])
  

  // >> Hàm xử lý submit
  const handleSubmitForm = async (e) => {
    e.preventDefault();

    // Tạo payload gửi lên server
    const date_of_birth = `${year}-${padZero(month)}-${padZero(day)}`;
    const payload = {
      full_name: `${firstName} ${lastName}`,
      email,
      password_hash: password,
      first_name: firstName,
      last_name: lastName,
      gender,
      date_of_birth,
      // avatar_url: "https://example.com/avatar.jpg",
      // cover_photo_url: "https://example.com/cover.jpg",
      // bio: "Hello, I am Quang",
      // school_id: "SV23110205",
      // academic_year: "2020-2024",
      // role: "student",
      // phone: "+84-912345678",
      // website: "https://diep.dev",
      // country: "Vietnam",
      // city: "HCMC",
    };

    // Fetch API
    try {
    console.log(payload)
    const res = await fetch(import.meta.env.VITE_API_URL + "/users/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      throw new Error(`Lỗi HTTP: ${res.status}`);
    }

    const data = await res.json();
    console.log("Đăng ký thành công:", data);
    setAnnouncement({successtive: true, message: "Bạn đã đăng ký thành công!"})

    // Reset state 
    setFirstName("");
    setLastName("");
    // setDay("1");
    // setMonth("1");
    // setYear("2025");
    setEmail("");
    setPassword("");
    setGender("");

    } catch (err) {
      console.error("Lỗi khi gọi API:", err);
      setAnnouncement({successtive: false, message: `Lỗi khi gọi API: ${err}`})
    }

  }


  return (
    <div className="container-fluid bg-light vh-100 h-flex justify-content-center align-items-center">
      {/* Logo */}
      <div className="text-center mb-3">
        <h1 className="text-primary fw-bold display-6">QDTbook</h1>
      </div>

      {/* Content */}
      <div className="d-flex justify-content-center">
        <div className="card shadow p-3" style={{ maxWidth: "370px", width: "100%" }}>
          {/* Heading */}
          <h4 className="fw-bold text-center" style={{fontSize: "20px"}}>Create a new account</h4>
          <p className="text-center text-muted p-0 m-0" style={{fontSize: "12px"}}>It's quick and easy.</p>
          <hr className="p-0 mb-3 m-2"/>
  
          {/* Form */}
          <form>
            {/* First + Surname */}
            <div className="row">
              <div className="col">
                <input
                  type="text"
                  className="form-control fs-7"
                  placeholder="First name"
                  value={firstName}
                  onChange={(e) => {setFirstName(e.target.value)}}
                />
              </div>
              <div className="col">
                <input
                  type="text"
                  className="form-control fs-7"
                  placeholder="Surname"
                  value={lastName}
                  onChange={(e) => {setLastName(e.target.value)}}
                />
              </div>
            </div>
  
            {/* Date of birth */}
            <div className="p-0 m-0">
              <span className="form-label mb-1 fs-8 m-0 p-0">Date of birth</span>
              <div className="row g-1 p-0 m-0">
                <div className="col">
                  <select className="form-select fs-7" onChange={(e) => {setDay(e.target.value)}}>
                    {days.map(d => <option key={d} value={d}>{d}</option>)}
                  </select>
                </div>
                <div className="col">
                  <select className="form-select fs-7" onChange={(e) => {setMonth(e.target.value)}}>
                    {months.map(m => <option key={m} value={m}>{m}</option>)}
                  </select>
                </div>
                <div className="col">
                  <select className="form-select fs-7"  onChange={(e) => {setYear(e.target.value)}}>
                    {years.map(y => <option key={y} value={y}>{y}</option>)}
                  </select>
                </div>
              </div>
            </div>
  
            {/* Gender */}
            <div className="mb-2">
              <label className="form-label fs-8">Gender</label>
              <div className="row g-1">
                {/* Female */}
                <div className="col p-1">
                  <div 
                    className="border rounded p-1 d-flex flex-row justify-content-between align-items-center cursor-pointer"
                    onClick={() => handleClickGender("female")}
                  >
                    <label 
                      className="form-check-label ms-2 fs-7 cursor-pointer" 
                    >
                      Female
                    </label>
                    <input
                      className=""
                      type="radio"
                      name="gender"
                      id="female"
                      checked={gender === "female"}
                      onChange={() => handleClickGender("female")} // <--- thêm onChange
                    />
                  </div>
                </div>
                {/* Male */}
                <div className="col p-1">
                  <div 
                    className="border rounded p-1 d-flex flex-row justify-content-between align-items-center cursor-pointer"
                    onClick={() => handleClickGender("male")}
                  >
                    <label 
                      className="form-check-label ms-2 fs-7 cursor-pointer" 
                    >
                      Male
                    </label>
                    <input
                      className=""
                      type="radio"
                      name="gender"
                      id="male"
                      checked={gender === "male"}
                      onChange={() => handleClickGender("male")} // <--- thêm onChange
                    />
                  </div>
                </div>
                {/* Custom */}
                <div className="col p-1">
                  <div 
                    className="border rounded p-1 d-flex flex-row justify-content-between align-items-center cursor-pointer"
                    onClick={() => handleClickGender("custom")}
                  >
                    <label 
                      className="form-check-label ms-2 fs-7 cursor-pointer" 
                    >
                      Custom
                    </label>
                    <input
                      className=""
                      type="radio"
                      name="gender"
                      id="custom"
                      checked={gender === "custom"}
                      onChange={() => handleClickGender("custom")} // <--- thêm onChange
                    />
                  </div>
                </div>

              </div>
                              
                {/* Custom expander */}
                {gender === "custom" && (
                  <div className="row g-1 m-1 mt-0">
                    <select className="form-select fs-7">
                      <option>She: "Wish her a happy birthday!"</option>
                      <option>He: "Wish him a happy birthday!"</option>
                      <option>They: "Wish them a happy birthday!"</option>
                    </select>
                  </div>
                )}

            </div>
  
            {/* Email */}
            <div className="mb-2">
              <input
                type="email"
                className="form-control fs-7"
                placeholder="Email address"
                value={email}
                onChange={(e) => {setEmail(e.target.value)}}
              />
            </div>

            {/* Password */}
            <div className="mb-2">
              <input
                type="password"
                className="form-control fs-7"
                placeholder="Password"
                value={password}
                onChange={(e) => {setPassword(e.target.value)}}
              />
            </div>
  
            {/* Submit button */}
            <div className="d-grid">
              <button
                type="submit"
                className="btn btn-success fw-bold fs-7"
                style={{ backgroundColor: "#00a400", borderColor: "#00a400" }}
                onClick={handleSubmitForm}
              >
                Sign Up
              </button>
            </div>
          </form>
  
          {/* Footer */}
          <div className="text-center mt-2">
            <a href="/login" className="text-primary text-decoration-none fs-7" onClick={(e) => {e.preventDefault(); navigate("/login");}}>
              Already have an account?
            </a>
          </div>
        </div>
      </div>
        
      {announcement && <Announcement text={announcement.message} successfull={announcement.successtive}></Announcement>}
    </div>
  );
}
