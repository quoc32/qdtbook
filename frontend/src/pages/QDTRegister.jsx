import React from "react";
import { useState, useEffect } from "react";
import { getDaysInMonth } from "date-fns";

export default function QDTRegister() {
  const [gender, setGender] = useState("");

  const handleClickGender = (_gender) => {
    setGender(_gender);
  }

  useEffect(() => {
    console.log(gender);
    return () => {
    };
  }, [gender]);

  const [year, setYear] = useState(2025);
  const [month, setMonth] = useState(1);
  const [day, setDay] = useState(1);
  const [days, setDays] = useState([]);
  const years = Array.from({ length: 2025 - 1905 + 1 }, (_, i) => 2025 - i);
  const months = Array.from({ length: 12 }, (_, i) => i + 1);
  useEffect(() => {
    setDays(Array.from({ length: getDaysInMonth(new Date(year, month - 1)) }, (_, i) => i + 1));
  }, [year, month]);

  return (
    <div className="container-fluid bg-light vh-100 h-flex justify-content-center align-items-center">
      {/* Logo */}
      <div className="text-center mb-3">
        <h1 className="text-primary fw-bold display-6">QDTbook</h1>
      </div>

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
                />
              </div>
              <div className="col">
                <input
                  type="text"
                  className="form-control fs-7"
                  placeholder="Surname"
                />
              </div>
            </div>
  
            {/* Date of birth */}
            <div className="p-0 m-0">
              <span className="form-label mb-1 fs-8 m-0 p-0">Date of birth</span>
              <div className="row g-1 p-0 m-0">
                <div className="col">
                  <select className="form-select fs-7">
                    {days.map(d => <option key={d} value={d}>{d}</option>)}
                  </select>
                </div>
                <div className="col">
                  <select className="form-select fs-7">
                    {months.map(m => <option key={m} value={m}>{m}</option>)}
                  </select>
                </div>
                <div className="col">
                  <select className="form-select fs-7">
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
  
            {/* Email + Password */}
            <div className="mb-2">
              <input
                type="email"
                className="form-control fs-7"
                placeholder="Mobile number or email address"
              />
            </div>
            <div className="mb-2">
              <input
                type="password"
                className="form-control fs-7"
                placeholder="New password"
              />
            </div>
  
            {/* Terms */}
            {/* <p className="small text-muted fs-7">
              People who use our service may have uploaded your contact
              information to Facebook.{" "}
              <a href="#" className="text-decoration-none">
                Learn more.
              </a>
            </p>
            <p className="small text-muted fs-7">
              By clicking Sign Up, you agree to our{" "}
              <a href="#" className="text-decoration-none">Terms</a>,{" "}
              <a href="#" className="text-decoration-none">Privacy Policy</a> and{" "}
              <a href="#" className="text-decoration-none">Cookies Policy</a>. You
              may receive SMS notifications from us and can opt out at any time.
            </p> */}
  
            {/* Submit button */}
            <div className="d-grid">
              <button
                type="submit"
                className="btn btn-success fw-bold fs-7"
                style={{ backgroundColor: "#00a400", borderColor: "#00a400" }}
              >
                Sign Up
              </button>
            </div>
          </form>
  
          {/* Footer */}
          <div className="text-center mt-2">
            <a href="/login" className="text-primary text-decoration-none fs-7">
              Already have an account?
            </a>
          </div>
        </div>
      </div>

    </div>
  );
}
