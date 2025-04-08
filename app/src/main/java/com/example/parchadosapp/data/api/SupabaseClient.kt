package com.example.parchadosapp.data.api

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://giynykejishwdmsgshag.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdpeW55a2VqaXNod2Rtc2dzaGFnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDAyOTE2MDQsImV4cCI6MjA1NTg2NzYwNH0.LIbJB6-QqfwQVZ5Oq44japsF0Lu4AOc5kNgPi1_IHRo"
) {
    install(Postgrest)
}
