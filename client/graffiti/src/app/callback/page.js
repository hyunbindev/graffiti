"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useState, useEffect } from "react";

export default function AuthenticationCallback() {
    const [token, setToken] = useState(null);
    const router = useRouter();
    const searchParams = useSearchParams();

    useEffect(()=>{
        let token = searchParams.get("accessToken");
        setToken(token);
    },[]);


    return (
        <div>
            {token}
        </div>
    );
}
