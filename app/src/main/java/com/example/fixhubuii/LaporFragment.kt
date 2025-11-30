package com.example.fixhubuii

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.fixhubuii.databinding.FragmentLaporBinding
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*

class LaporFragment : Fragment() {

    private var _binding: FragmentLaporBinding? = null
    private val binding get() = _binding!!

    private val selectedImages = mutableListOf<Uri>()
    private val maxImages = 3

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    private val calendar = Calendar.getInstance()

    private var isIndoor = true

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { addImage(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaporBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupKategoriDropdown()
        setupLokasiToggle()
        setupFakultasDropdown()
        setupGedungDropdown()
        setupDatePicker()
        setupImageUpload()
        setupButtons()
    }

    private fun setupKategoriDropdown() {
        val data = resources.getStringArray(R.array.kategori_barang)
        binding.actvKategori.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, data)
        )
    }

    private fun setupLokasiToggle() {
        setLokasiType(true)

        binding.btnIndoor.setOnClickListener { setLokasiType(true) }
        binding.btnOutdoor.setOnClickListener { setLokasiType(false) }
    }

    private fun setLokasiType(indoor: Boolean) {
        isIndoor = indoor

        if (indoor) {
            binding.btnIndoor.apply {
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue_500)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            }
            binding.btnOutdoor.apply {
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey_200)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_600))
            }

            binding.layoutIndoor.visibility = View.VISIBLE
            binding.layoutOutdoor.visibility = View.GONE
            binding.etLokasiOutdoor.text?.clear()
        } else {
            binding.btnOutdoor.apply {
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue_500)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            }
            binding.btnIndoor.apply {
                backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.grey_200)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_600))
            }

            binding.layoutIndoor.visibility = View.GONE
            binding.layoutOutdoor.visibility = View.VISIBLE
        }
    }

    private fun setupFakultasDropdown() {
        val data = resources.getStringArray(R.array.fakultas_list)
        binding.actvFakultas.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, data)
        )
    }

    private fun setupGedungDropdown() {
        val data = resources.getStringArray(R.array.gedung_list)
        binding.actvGedung.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, data)
        )
    }

    private fun setupDatePicker() {
        binding.etTanggal.setOnClickListener { showDatePicker() }
        binding.tilTanggal.setEndIconOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                binding.etTanggal.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    private fun setupImageUpload() {
        binding.btnUploadFoto.setOnClickListener {
            if (selectedImages.size >= maxImages) {
                Toast.makeText(requireContext(), "Maksimal $maxImages foto", Toast.LENGTH_SHORT).show()
            } else {
                pickImageLauncher.launch("image/*")
            }
        }
    }

    private fun addImage(uri: Uri) {
        selectedImages.add(uri)
        updateImagePreview()
    }

    private fun updateImagePreview() {
        binding.layoutImages.removeAllViews()

        if (selectedImages.isEmpty()) {
            binding.layoutImagePreview.visibility = View.GONE
            return
        }

        binding.layoutImagePreview.visibility = View.VISIBLE

    }

    private fun setupButtons() {
        binding.btnBatal.setOnClickListener { showCancelConfirmation() }
        binding.btnKirim.setOnClickListener {
            if (validateForm()) submitLaporan()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        if (binding.etNamaBarang.text.isNullOrBlank()) {
            binding.tilNamaBarang.error = "Nama barang harus diisi"
            valid = false
        } else binding.tilNamaBarang.error = null

        if (binding.actvKategori.text.isNullOrBlank()) {
            binding.tilKategori.error = "Kategori harus dipilih"
            valid = false
        } else binding.tilKategori.error = null

        if (isIndoor) {
            if (binding.actvFakultas.text.isNullOrBlank()) {
                binding.tilFakultas.error = "Fakultas wajib"
                valid = false
            } else binding.tilFakultas.error = null

            if (binding.actvGedung.text.isNullOrBlank()) {
                binding.tilGedung.error = "Gedung wajib"
                valid = false
            } else binding.tilGedung.error = null

            if (binding.etRuangan.text.isNullOrBlank()) {
                binding.tilRuangan.error = "Ruangan wajib"
                valid = false
            } else binding.tilRuangan.error = null
        } else {
            if (binding.etLokasiOutdoor.text.isNullOrBlank()) {
                binding.tilLokasiOutdoor.error = "Lokasi harus diisi"
                valid = false
            } else binding.tilLokasiOutdoor.error = null
        }

        if (binding.rgTingkatKerusakan.checkedRadioButtonId == -1) {
            Toast.makeText(requireContext(), "Pilih tingkat kerusakan", Toast.LENGTH_SHORT).show()
            valid = false
        }

        if (binding.etDeskripsi.text.isNullOrBlank()) {
            binding.tilDeskripsi.error = "Deskripsi wajib"
            valid = false
        } else binding.tilDeskripsi.error = null

        if (binding.etTanggal.text.isNullOrBlank()) {
            binding.tilTanggal.error = "Tanggal wajib"
            valid = false
        } else binding.tilTanggal.error = null

        return valid
    }

    private fun submitLaporan() {
        Toast.makeText(requireContext(), "Laporan berhasil dikirim!", Toast.LENGTH_LONG).show()
        requireActivity().onBackPressed()
    }

    private fun showCancelConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Batalkan Laporan?")
            .setMessage("Data yang sudah diisi akan hilang.")
            .setPositiveButton("Ya") { _, _ ->
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
